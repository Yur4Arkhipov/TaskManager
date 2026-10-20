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
import com.jacqulin.taskmanager.core.domain.voice.VoiceInputUseCase
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionFailure
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toDomain
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toUiModel
import com.jacqulin.taskmanager.feature.tasks.presentation.model.DraftTaskUi
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TasksScreenViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val cancelVoiceRecordingUseCase: CancelVoiceRecordingUseCase,
    private val voiceInputUseCase: VoiceInputUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val sortType = MutableStateFlow(SortType.NEW_TO_OLD)
    private val taskFilter = MutableStateFlow(TaskFilter.ALL)
    private val draftTask = MutableStateFlow<DraftTaskUi?>(null)
    private val voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)

    private val _effects = MutableSharedFlow<TasksEffect>()
    val effects: SharedFlow<TasksEffect> = _effects.asSharedFlow()

    private val tasksUiState: StateFlow<TasksUiState> =
        combine(
            observeTasksUseCase(),
            searchQuery,
            sortType,
            taskFilter,
            draftTask
        ) { tasks, searchQuery, sortType, taskFilter, draftTask ->
            val visibleTasks = tasks
                .map { it.toUiModel() }
                .filter { task ->
                    when (taskFilter) {
                        TaskFilter.ALL -> true
                        TaskFilter.ACTIVE -> !task.isCompleted
                        TaskFilter.COMPLETED -> task.isCompleted
                    }
                }
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
                taskFilter = taskFilter,
                sortType = sortType,
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
            is TasksEvent.OnTaskFilterChanged -> {
                taskFilter.value = event.filter
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
                log("Stop voice input clicked")
                setVoiceState(VoiceState.Processing)
                viewModelScope.launch {
                    voiceInputUseCase.stop()
                        .onSuccess { text ->
                            if (text.isNotBlank()) {
//                                appendRecognizedText(text)
                                draftTask.value = DraftTaskUi(title = text)
                                setVoiceState(VoiceState.Success(text))
                            }
                        }
                        .onFailure {
                            setVoiceState(
                                VoiceState.Error(VoiceError.RecognitionFailed))
                        }
//                    stopAndRecognizeVoiceUseCase()
//                        .onSuccess { text ->
//                            voiceState.value = VoiceState.Success(text)
//                            delay(3000.milliseconds)
//                            draftTask.value = DraftTaskUi(title = text)
//                            voiceState.value = VoiceState.Idle
//
//                        }
//                        .onFailure { error ->
//                            Log.e("TasksVM", "[TasksEvent.VoiceInputStopClicked] Failed to record and recognize voice", error)
//                            voiceState.value = VoiceState.Error
//                            emitEffect(TasksEffect.ShowError("Ошибка с обработкой речи"))
//                        }
                }
            }
            is TasksEvent.VoicePermissionGranted -> {
                log("Voice permission granted")
                startSpeechRecognition()
//                viewModelScope.launch {
//                    startVoiceRecordingUseCase()
//                        .onSuccess {
//                            voiceState.value = VoiceState.Recording
//                        }
//                        .onFailure { error ->
//                            Log.e("TasksVM", "[TasksEvent.VoicePermissionGranted] Failed to start voice recording", error)
//                            voiceState.value = VoiceState.Error
//                            emitEffect(TasksEffect.ShowError("Возникала ошибка при попытке записи речи"))
//                        }
//                }
            }
            TasksEvent.VoicePermissionDenied -> {
                log("Voice permission denied")
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
                log("Retry voice input clicked")
                startSpeechRecognition()
//                viewModelScope.launch {
//                    startVoiceRecordingUseCase()
//                        .onSuccess {
//                            voiceState.value = VoiceState.Recording
//                        }
//                        .onFailure { error ->
//                            Log.e("TasksVM", "[TasksEvent.VoiceInputRetry] Failed to start voice recording", error)
//                            voiceState.value = VoiceState.Error
//                            emitEffect(TasksEffect.ShowError("Возникала ошибка при попытке записи речи"))
//                        }
//                }
            }
            TasksEvent.VoiceInputCancel -> {
                log("cancel voice recording")
                viewModelScope.launch {
                    cancelVoiceRecordingUseCase()
                    setVoiceState(VoiceState.Idle)
                }
            }
        }
    }

    private fun startSpeechRecognition() {
        setVoiceState(VoiceState.Recording())

        viewModelScope.launch {
            voiceInputUseCase.start()
                .onFailure {
                    setVoiceState(
                        VoiceState.Error(
                            VoiceError.RecordingFailed))
                    return@launch
                }
            voiceInputUseCase.listen().collect { event ->
                when (event) {
                    SpeechRecognitionEvent.Ready,
                    SpeechRecognitionEvent.SpeechStarted -> {
                        if (uiState.value.voiceState !is VoiceState.Recording) {
                            setVoiceState(VoiceState.Recording())
                        }
                    }

                    SpeechRecognitionEvent.SpeechEnded -> {
                        setVoiceState(VoiceState.Processing)
                    }

                    is SpeechRecognitionEvent.PartialResult -> {
                        setVoiceState(VoiceState.Recording(event.text))
                    }

                    is SpeechRecognitionEvent.Result -> {
//                        appendRecognizedText(event.text)
                        draftTask.value = DraftTaskUi(title = event.text)
                        setVoiceState(VoiceState.Success(event.text))
                        delay(3000.milliseconds)
                        setVoiceState(VoiceState.Idle)
                    }

                    is SpeechRecognitionEvent.Error -> {
                        setVoiceState(VoiceState.Error(event.reason.toVoiceError()))
                    }
                }
            }
        }
    }

    private fun SpeechRecognitionFailure.toVoiceError(): VoiceError = when (this) {
        SpeechRecognitionFailure.ServiceUnavailable,
        SpeechRecognitionFailure.ServiceBusy,
        SpeechRecognitionFailure.TooManyRequests,
        SpeechRecognitionFailure.UnsupportedLanguage -> VoiceError.RecognitionFailed
        SpeechRecognitionFailure.NoMatch -> VoiceError.EmptyResult
        SpeechRecognitionFailure.Network -> VoiceError.Network
        SpeechRecognitionFailure.AudioInput,
        SpeechRecognitionFailure.PermissionDenied -> VoiceError.RecordingFailed
        SpeechRecognitionFailure.Unknown -> VoiceError.Unknown
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

    private fun setVoiceState(state: VoiceState) {
        voiceState.update { state }
    }

    private fun log(message: String) {
        Log.d(TAG, "[${Thread.currentThread().name}] $message")
    }

    private companion object {
        const val TAG = "TasksVM"
    }
}
