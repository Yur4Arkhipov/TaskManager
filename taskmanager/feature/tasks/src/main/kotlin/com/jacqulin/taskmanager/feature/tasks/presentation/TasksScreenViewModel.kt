package com.jacqulin.taskmanager.feature.tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.core.voice.domain.VoiceError
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import com.jacqulin.taskmanager.core.voice.domain.VoiceState
import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.DeleteTaskUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.ObserveTasksUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.SaveTaskUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.UpdateTaskStatusUseCase
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toDomain
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toUiModel
import com.jacqulin.taskmanager.feature.tasks.presentation.model.DraftTaskUi
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
class TasksScreenViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val voiceRecognizer: VoiceRecognizer
) : ViewModel() {

    private val searchQueryInput = MutableStateFlow("")
    private val appliedSearchQuery = MutableStateFlow("")
    private val sortType = MutableStateFlow(SortType.NEW_TO_OLD)

    private val _draftTask = MutableStateFlow<DraftTaskUi?>(null)

    private val _effects = MutableSharedFlow<TasksEffect>()
    val effects: SharedFlow<TasksEffect> = _effects.asSharedFlow()

    private val _voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)

    private val tasksUiState =
        combine(
            observeTasksUseCase(),
            searchQueryInput,
            appliedSearchQuery,
            sortType,
            _draftTask
        ) { tasks, inputQuery, appliedQuery, sortType, draftTask ->

            val visibleTasks = tasks
                .map { it.toUiModel() }
                .filter { task ->
                    val query = appliedQuery.trim()
                    query.isBlank() ||
                            task.title.contains(query, ignoreCase = true)
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
                searchQueryInput = inputQuery,
                appliedSearchQuery = appliedQuery,
                visibleTasks = visibleTasks,
                isEmpty = visibleTasks.isEmpty(),
                draftTask = draftTask
            )
        }

    val uiState: StateFlow<TasksUiState> =
        combine(
            tasksUiState,
            _voiceState
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
                searchQueryInput.value = event.query
            }
            TasksEvent.OnSearchSubmitted -> {
                appliedSearchQuery.value = searchQueryInput.value
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
                emitEffect(TasksEffect.RequestVoicePermission)
            }
            TasksEvent.VoiceInputStopClicked -> {
                _voiceState.value = VoiceState.Processing
                viewModelScope.launch {
                    val result = voiceRecognizer.stopAndRecognize()

                    result.onSuccess { text ->
                        _draftTask.value = DraftTaskUi(title = text)
                        _voiceState.value = VoiceState.Success(text)
                    }.onFailure { error ->
                        _voiceState.value = VoiceState.Error(VoiceError.Unknown)
                        emitEffect(TasksEffect.ShowError("Не удалось распознать речь"))
                    }
                }
            }
            TasksEvent.VoicePermissionGranted -> {
                try {
                    voiceRecognizer.start()
                    _voiceState.value = VoiceState.Recording
                } catch (e: Exception) {
                    _voiceState.value = VoiceState.Error(VoiceError.Network)
                    emitEffect(TasksEffect.ShowError("Не удалось начать запись"))
                }
            }
            TasksEvent.VoicePermissionDenied -> {
                emitEffect(
                    TasksEffect.ShowError("Для распознавания речи необходимо предоставить разрешение на запись аудио")
                )
            }
            is TasksEvent.VoiceTextRecognized -> {
                _draftTask.value = DraftTaskUi(title = event.text)
                _voiceState.value = VoiceState.Success(text = event.text)
            }
            is TasksEvent.OnDraftTaskTextChanged -> {
                _draftTask.value = _draftTask.value?.copy(
                    title = event.text
                )
            }
            TasksEvent.OnDraftTaskSaveClicked -> {
                saveDraftTask()
            }
            TasksEvent.OnDraftTaskDeleteClicked -> {
                _draftTask.value = null
            }
            is TasksEvent.UpdateTaskStatus -> {
                updateTaskStatus(event.task)
            }
            TasksEvent.VoiceInputDismissed -> {
                voiceRecognizer.cancel()
                _voiceState.value = VoiceState.Idle
            }
            TasksEvent.VoiceInputRetry -> {
                _voiceState.value = VoiceState.Recording
                try {
                    voiceRecognizer.start()
                } catch (e: Exception) {
                    _voiceState.value = VoiceState.Error(VoiceError.Unknown)
                    emitEffect(TasksEffect.ShowError("Не удалось начать запись"))
                }
            }
            TasksEvent.VoiceInputCancel -> {
                voiceRecognizer.cancel()
                _voiceState.value = VoiceState.Idle
            }
        }
    }

    private fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
        }
    }

    private fun createDraftTask() {
        _draftTask.value = DraftTaskUi()
    }

    private fun saveDraftTask() {
        val draft = _draftTask.value ?: return
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

            _draftTask.value = null
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
