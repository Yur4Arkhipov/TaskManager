package com.jacqulin.taskmanager.feature.tasks.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.core.domain.tasks.DeleteTaskUseCase
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.core.domain.tasks.SaveTaskUseCase
import com.jacqulin.taskmanager.core.domain.tasks.UpdateTaskStatusUseCase
import com.jacqulin.taskmanager.core.domain.voice.CancelVoiceRecordingUseCase
import com.jacqulin.taskmanager.core.domain.voice.StartVoiceRecordingUseCase
import com.jacqulin.taskmanager.core.domain.voice.StopAndRecognizeVoiceUseCase
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toDomain
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toUiModel
import com.jacqulin.taskmanager.feature.tasks.presentation.model.DraftTaskUi
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TasksScreenViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val startVoiceRecordingUseCase: StartVoiceRecordingUseCase,
    private val stopAndRecognizeVoiceUseCase: StopAndRecognizeVoiceUseCase,
    private val cancelVoiceRecordingUseCase: CancelVoiceRecordingUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortType = MutableStateFlow(SortType.NEW_TO_OLD)
    private val draftTask = MutableStateFlow<DraftTaskUi?>(null)
    private val voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)

    private val _effects = MutableSharedFlow<TasksEffect>()
    val effects: SharedFlow<TasksEffect> = _effects.asSharedFlow()

    private val tasksUiState: StateFlow<TasksUiState> =
        combine(
            observeTasksUseCase(),
            searchQuery,
            sortType,
            draftTask
        ) { tasks, searchQuery, sortType, draftTask ->
            val visibleTasks = tasks
                .map { it.toUiModel() }
                .filter { task ->
                    val query = searchQuery.trim()
                    query.isBlank() || task.title.contains(query, ignoreCase = true)
                }
                .let { tasks ->
                    when (sortType) {
                        SortType.NEW_TO_OLD -> {
                            tasks.sortedWith(
                                compareBy<TaskItemUi> { it.isCompleted }
                                    .thenByDescending { it.createdAtMillis }
                            )
                        }
                        SortType.OLD_TO_NEW -> {
                            tasks.sortedWith(
                                compareBy<TaskItemUi> { it.isCompleted }
                                    .thenBy { it.createdAtMillis }
                            )
                        }
                    }
                }
            TasksUiState(
                searchQuery = searchQuery,
                visibleTasks = visibleTasks,
                isEmpty = visibleTasks.isEmpty(),
                draftTask = draftTask
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            TasksUiState(),
        )

    val uiState: StateFlow<TasksUiState> =
        combine(
            tasksUiState,
            voiceState
        ) { state, voiceState ->
            state.copy(voiceState = voiceState)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            TasksUiState()
        )

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.OnSearchQueryChanged -> {
                searchQuery.value = event.query
            }
            is TasksEvent.OnSortChanged -> {
                sortType.value = event.sortType
            }
            is TasksEvent.OnDeleteTaskClicked -> {
                deleteTask(event.taskId)
            }
            TasksEvent.OnCreateTaskByTextClicked -> {
                createDraftTask()
            }
            TasksEvent.OnCreateTaskByVoiceClicked -> {
                Log.d("TasksVM", "[TasksEvent.OnCreateTaskByVoiceClicked] need voice permission")
                emitEffect(TasksEffect.RequestVoicePermission)
            }
            is TasksEvent.VoiceInputStopClicked -> {
                Log.d("TasksVM", "[TasksEvent.VoiceInputStopClicked] stop voice recording and start recognize")
                Log.d("TasksVM", "[TasksEvent.VoiceInputStopClicked] ${Thread.currentThread().name}")
                voiceState.value = VoiceState.Processing
                viewModelScope.launch {
                    stopAndRecognizeVoiceUseCase()
                        .onSuccess { text ->
                            voiceState.value = VoiceState.Success(text)
                            delay(3000.milliseconds)
                            draftTask.value = DraftTaskUi(title = text)
                            voiceState.value = VoiceState.Idle

                        }
                        .onFailure { error ->
                            Log.e("TasksVM", "[TasksEvent.VoiceInputStopClicked] Failed to record and recognize voice", error)
                            voiceState.value = VoiceState.Error
                            emitEffect(TasksEffect.ShowError("Ошибка с обработкой речи"))
                        }
                }
            }
            is TasksEvent.VoicePermissionGranted -> {
                Log.d("TasksVM", "[TasksEvent.VoicePermissionGranted] start voice recording")
                Log.d("TasksVM", "[TasksEvent.VoicePermissionGranted] ${Thread.currentThread().name}")
                viewModelScope.launch {
                    startVoiceRecordingUseCase()
                        .onSuccess {
                            voiceState.value = VoiceState.Recording
                        }
                        .onFailure { error ->
                            Log.e("TasksVM", "[TasksEvent.VoicePermissionGranted] Failed to start voice recording", error)
                            voiceState.value = VoiceState.Error
                            emitEffect(TasksEffect.ShowError("Возникала ошибка при попытке записи речи"))
                        }
                }
            }
            TasksEvent.VoicePermissionDenied -> {
                emitEffect(TasksEffect.ShowError("Для распознавания речи необходимо предоставить разрешение на запись аудио"))
            }
            is TasksEvent.OnDraftTaskTextChanged -> {
                draftTask.value = draftTask.value?.copy(
                    title = event.text
                )
            }
            TasksEvent.OnDraftTaskSaveClicked -> {
                saveDraftTask()
            }
            TasksEvent.OnDraftTaskDeleteClicked -> {
                draftTask.value = null
            }
            is TasksEvent.UpdateTaskStatus -> {
                updateTaskStatus(event.task)
            }
            TasksEvent.VoiceInputRetry -> {
                Log.d("TasksVM", "[TasksEvent.VoiceInputRetry] retry voice recording")
                Log.d("TasksVM", "[TasksEvent.VoiceInputRetry] ${Thread.currentThread().name}")
                viewModelScope.launch {
                    startVoiceRecordingUseCase()
                        .onSuccess {
                            voiceState.value = VoiceState.Recording
                        }
                        .onFailure { error ->
                            Log.e("TasksVM", "[TasksEvent.VoiceInputRetry] Failed to start voice recording", error)
                            voiceState.value = VoiceState.Error
                            emitEffect(TasksEffect.ShowError("Возникала ошибка при попытке записи речи"))
                        }
                }
            }
            TasksEvent.VoiceInputCancel -> {
                Log.d("TasksVM", "[TasksEvent.VoiceInputCancel] cancel voice recording")
                Log.d("TasksVM", "[TasksEvent.VoiceInputCancel] ${Thread.currentThread().name}")
                viewModelScope.launch {
                    cancelVoiceRecordingUseCase()
                    voiceState.value = VoiceState.Idle
                }
            }
        }
    }

    private fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
        }
    }

    private fun createDraftTask() {
        draftTask.value = DraftTaskUi()
    }

    private fun saveDraftTask() {
        val draft = draftTask.value ?: return
        val title = draft.title.trim()

        if (title.isBlank()) return

        viewModelScope.launch {
            saveTaskUseCase(
                Task(
                    id = 0,
                    title = title,
                    createdAtMillis = System.currentTimeMillis(),
                    isCompleted = false,
                )
            )

            draftTask.value = null
        }
    }

    private fun updateTaskStatus(task: TaskItemUi) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                isCompleted = !task.isCompleted
            )

            updateTaskStatusUseCase(updatedTask.toDomain())
        }
    }

    private fun emitEffect(effect: TasksEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}
