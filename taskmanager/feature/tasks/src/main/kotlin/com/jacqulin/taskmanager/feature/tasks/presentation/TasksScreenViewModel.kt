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
import com.jacqulin.taskmanager.feature.tasks.presentation.state.MainScreenState
import com.jacqulin.taskmanager.feature.tasks.presentation.state.TaskState
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

    private val mainScreenState = MutableStateFlow(MainScreenState())
    private val taskState = MutableStateFlow(TaskState())
    private val voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)

    private val _effects = MutableSharedFlow<TasksEffect>()
    val effects: SharedFlow<TasksEffect> = _effects.asSharedFlow()

    private val tasksUiState: StateFlow<TasksUiState> =
        combine(
            observeTasksUseCase(),
            mainScreenState,
            taskState,
        ) { tasks, mainScreenState, taskState ->
            val visibleTasks = tasks
                .map { it.toUiModel() }
                .filter { task ->
                    when (mainScreenState.filter) {
                        TaskFilter.ALL -> true
                        TaskFilter.ACTIVE -> !task.isCompleted
                        TaskFilter.COMPLETED -> task.isCompleted
                    }
                }
                .filter { task ->
                    val query = mainScreenState.searchQuery.trim()
                    query.isBlank() || task.title.contains(query, ignoreCase = true)
                }
                .let { tasks ->
                    when (mainScreenState.sortType) {
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
                mainScreenState = mainScreenState,
                taskState = taskState,
                visibleTasks = visibleTasks,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            TasksUiState(),
        )

    val uiState: StateFlow<TasksUiState> =
        combine(
            tasksUiState,
            voiceState,
        ) { state, voiceState ->
            state.copy(
                voiceState = voiceState,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            TasksUiState()
        )

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.OnSearchQueryChanged -> {
                mainScreenState.update { it.copy(searchQuery = event.query) }
            }
            is TasksEvent.OnSortChanged -> {
                mainScreenState.update { it.copy(sortType = event.sortType) }
            }
            is TasksEvent.OnTaskFilterChanged -> {
                mainScreenState.update { it.copy(filter = event.filter) }
            }
            is TasksEvent.UpdateTaskStatus -> {
                updateTaskStatus(event.task)
            }
            is TasksEvent.OnDeleteTaskClicked -> {
                deleteTask(event.taskId)
            }
            TasksEvent.OnSaveTaskClicked -> {
                saveTask()
            }
            TasksEvent.OnBottomSheetDismissed -> {
                mainScreenState.update { it.copy(isBottomSheetVisible = false) }
            }

            // Text
            TasksEvent.OnCreateTaskByTextClicked -> {
                mainScreenState.update { it.copy(isBottomSheetVisible = true) }
            }
            is TasksEvent.OnTaskTextChanged -> {
                taskState.update { it.copy(title = event.title) }
            }
            is TasksEvent.OnTaskTimeEstimateSelected -> {

            }
            is TasksEvent.OnTaskPlanForTodayChanged -> {

            }
            TasksEvent.OnTaskAddTagClicked -> {

            }

            // Voice
            is TasksEvent.VoiceInputStopClicked -> {
                log("Stop voice input clicked")
                setVoiceState(VoiceState.Processing)
                viewModelScope.launch {
                    voiceInputUseCase.stop()
                        .onSuccess { text ->
                            if (text.isNotBlank()) {
//                                appendRecognizedText(text)
                                taskState.update { it.copy(title = text) }
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
            TasksEvent.OnCreateTaskByVoiceClicked -> {
                Log.d("TasksVM", "[TasksEvent.OnCreateTaskByVoiceClicked] need voice permission")
                emitEffect(TasksEffect.RequestVoicePermission)
            }
            TasksEvent.VoicePermissionDenied -> {
                log("Voice permission denied")
                emitEffect(TasksEffect.ShowError("Для распознавания речи необходимо предоставить разрешение на запись аудио"))
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
                        taskState.update { it.copy(title = event.text) }
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

    private fun saveTask() {
        viewModelScope.launch {
            saveTaskUseCase(
                Task(
                    id = 0,
                    title = taskState.value.title,
                    createdAtMillis = System.currentTimeMillis(),
                    isCompleted = false,
                )
            )
        }

        onEvent(TasksEvent.OnBottomSheetDismissed)
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
