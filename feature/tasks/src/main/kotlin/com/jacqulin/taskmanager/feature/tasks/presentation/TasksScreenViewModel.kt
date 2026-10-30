package com.jacqulin.taskmanager.feature.tasks.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.core.domain.tags.ObserveTagsUseCase
import com.jacqulin.taskmanager.core.domain.tasks.DeleteTaskUseCase
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.core.domain.tasks.SaveTaskUseCase
import com.jacqulin.taskmanager.core.domain.tasks.UpdateTaskStatusUseCase
import com.jacqulin.taskmanager.core.domain.voice.CancelVoiceRecordingUseCase
import com.jacqulin.taskmanager.core.domain.voice.VoiceInputUseCase
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionFailure
import com.jacqulin.taskmanager.feature.tasks.presentation.model.MainScreenState
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskState
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TasksEffect
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TasksEvent
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TasksUiState
import com.jacqulin.taskmanager.feature.tasks.presentation.model.toDomain
import com.jacqulin.taskmanager.feature.tasks.presentation.model.toUiModel
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
    observeTagsUseCase: ObserveTagsUseCase,
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
            observeTagsUseCase(),
            mainScreenState,
            taskState,
        ) { tasks, tags, mainScreenState, taskState ->
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
                availableTags = tags,
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
                mainScreenState.update { if (it.isDeleteModeEnabled) it else it.copy(searchQuery = event.query) }
            }
            is TasksEvent.OnSortChanged -> {
                mainScreenState.update { if (it.isDeleteModeEnabled) it else it.copy(sortType = event.sortType) }
            }
            is TasksEvent.OnTaskFilterChanged -> {
                mainScreenState.update { if (it.isDeleteModeEnabled) it else it.copy(filter = event.filter) }
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
                taskState.update { TaskState() }
            }
            TasksEvent.OnDeleteModeToggled -> {
                mainScreenState.update {
                    it.copy(isDeleteModeEnabled = !it.isDeleteModeEnabled, selectedTaskIds = emptySet())
                }
            }
            is TasksEvent.OnTaskSelectionToggled -> mainScreenState.update { state ->
                if (!state.isDeleteModeEnabled) state else state.copy(
                    selectedTaskIds = state.selectedTaskIds
                        .toMutableSet()
                        .apply { if (!add(event.taskId)) remove(event.taskId) }
                )
            }
            is TasksEvent.OnEnterDeleteMode -> mainScreenState.update {
                it.copy(isDeleteModeEnabled = true, selectedTaskIds = setOf(event.taskId))
            }
            TasksEvent.OnSelectAllTasks -> mainScreenState.update { state ->
                state.copy(
                    selectedTaskIds = state.selectedTaskIds
                        .toMutableSet()
                        .let { selected ->
                            val visibleIds = uiState.value.visibleTasks
                                .map { it.id }
                                .toSet()
                            if (selected.containsAll(visibleIds))
                                selected.removeAll(visibleIds)
                            else
                                selected.addAll(visibleIds)
                            selected
                        }
                )
            }
            TasksEvent.OnDeleteSelectedTasks -> {
                val ids = mainScreenState.value.selectedTaskIds.toList()
                ids.forEach(::deleteTask)
                mainScreenState.update { it.copy(isDeleteModeEnabled = false, selectedTaskIds = emptySet()) }
            }
            is TasksEvent.OnShowCreationDateChanged -> mainScreenState.update { it.copy(showCreationDate = event.enabled) }
            is TasksEvent.OnShowTagsChanged -> mainScreenState.update { it.copy(showTags = event.enabled) }
            is TasksEvent.OnCompactViewChanged -> mainScreenState.update { it.copy(isCompactView = event.enabled) }

            // Text
            TasksEvent.OnCreateTaskByTextClicked -> {
                mainScreenState.update { it.copy(isBottomSheetVisible = true) }
            }
            is TasksEvent.OnTaskTextChanged -> {
                taskState.update { it.copy(title = event.title) }
            }
            is TasksEvent.OnTaskTimeEstimateSelected -> {

            }
            is TasksEvent.OnTaskPlanForTodayChanged -> taskState.update {
                it.copy(isPlannedForToday = event.enabled)
            }
            is TasksEvent.OnTaskTagAdded -> taskState.update { state ->
                state.copy(tags = state.tags + event.tag)
            }
            is TasksEvent.OnTaskTagRemoved -> taskState.update { state ->
                state.copy(tags = state.tags - event.tag)
            }

            // Voice
            is TasksEvent.VoiceInputStopClicked -> {
                log("Stop voice input clicked")
                setVoiceState(VoiceState.Processing)
                viewModelScope.launch {
                    voiceInputUseCase.stop()
                        .onSuccess { text ->
                            showRecognizedTextInSheet(text)
                        }
                        .onFailure {
                            setVoiceState(VoiceState.Error(VoiceError.RecognitionFailed))
                        }
                }
            }
            is TasksEvent.VoicePermissionGranted -> {
                log("Voice permission granted")
                startSpeechRecognition()

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
                    setVoiceState(VoiceState.Error(VoiceError.RecordingFailed))
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
                        showRecognizedTextInSheet(event.text)
                    }

                    is SpeechRecognitionEvent.Error -> {
                        setVoiceState(VoiceState.Error(event.reason.toVoiceError()))
                    }
                }
            }
        }
    }

    private fun setVoiceState(state: VoiceState) {
        voiceState.update { state }
    }

    private suspend fun showRecognizedTextInSheet(text: String) {
        if (text.isBlank()) return

        taskState.update { it.copy(title = text) }
        setVoiceState(VoiceState.Success(text))
        delay(3_000.milliseconds)
        setVoiceState(VoiceState.Idle)
        mainScreenState.update { it.copy(isBottomSheetVisible = true) }
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
                    tags = taskState.value.tags,
                    isPlannedForToday = taskState.value.isPlannedForToday,
                    dayPlanOrder = null
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

    private fun log(message: String) {
        Log.d(TAG, "[${Thread.currentThread().name}] $message")
    }

    private companion object {
        const val TAG = "TasksVM"
    }
}
