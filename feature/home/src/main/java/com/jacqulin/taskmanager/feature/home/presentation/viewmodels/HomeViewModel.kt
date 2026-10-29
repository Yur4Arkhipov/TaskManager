package com.jacqulin.taskmanager.feature.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.tags.ObserveTagsUseCase
import com.jacqulin.taskmanager.core.domain.tasks.GetAllTasksUseCase
import com.jacqulin.taskmanager.core.domain.tasks.GetTaskByIdUseCase
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.core.domain.tasks.SaveTaskUseCase
import com.jacqulin.taskmanager.core.domain.tasks.UpdateDayPlanOrderUseCase
import com.jacqulin.taskmanager.core.domain.tasks.UpdateTaskStatusUseCase
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEffect
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeUiState
import com.jacqulin.taskmanager.feature.home.presentation.model.MainState
import com.jacqulin.taskmanager.feature.home.presentation.model.TaskState
import com.jacqulin.taskmanager.feature.home.presentation.model.toDomain
import com.jacqulin.taskmanager.feature.home.presentation.model.toHomeTaskUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    observeTagsUseCase: ObserveTagsUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val updateDayPlanOrderUseCase: UpdateDayPlanOrderUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects = _effects.asSharedFlow()

    private val mainState = MutableStateFlow(MainState())
    private val taskState = MutableStateFlow(TaskState())
    private val taskOrder = MutableStateFlow<List<Int>>(emptyList())
    private val dayPlanOrderCommitMutex = Mutex()
    private var isDayPlanOrderDirty = false

    val uiState: StateFlow<HomeUiState> =
        combine(
            observeTasksUseCase(),
            observeTagsUseCase(),
            mainState,
            taskState,
            taskOrder,
        ) { tasks, tags, mainState, taskState, taskOrder ->
            HomeUiState(
                mainState = mainState.copy(
                    dateLabel = dateLabel(),
                    availableTags = tags,
                    tasks = tasks
                        .filter { it.isPlannedForToday }
                        .map { task ->
                            HomeTaskUi(
                                id = task.id,
                                title = task.title,
                                durationMinutes = null,
                                isCompleted = task.isCompleted,
                                createdAtMillis = task.createdAtMillis,
                                tags = task.tags,
                                isPlannedForToday = task.isPlannedForToday,
                                dayPlanOrder = task.dayPlanOrder,
                            )
                        }
                        .let { tasks ->
                            val pendingOrderIndex = taskOrder.withIndex()
                                .associate { (index, id) -> id to index }
                            tasks.sortedWith(
                                compareBy<HomeTaskUi> { it.isCompleted }
                                    .thenBy {
                                        pendingOrderIndex[it.id]
                                            ?: it.dayPlanOrder
                                            ?: Int.MAX_VALUE
                                    }
                                    .thenByDescending { it.createdAtMillis }
                            )
                        },
                ),
                taskState = taskState
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(),
        )

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnTaskRemovedFromDayPlan -> {
                viewModelScope.launch {
                    val task = Task(
                        id = event.task.id,
                        title = event.task.title,
                        createdAtMillis = event.task.createdAtMillis,
                        isCompleted = event.task.isCompleted,
                        tags = event.task.tags,
                        isPlannedForToday = !event.task.isPlannedForToday,
                        dayPlanOrder = event.task.dayPlanOrder,
                    )
                    updateTaskStatusUseCase(task)
                }
            }
            is HomeEvent.UpdateTaskStatus -> {
                updateTaskStatus(event.task)
            }
            is HomeEvent.ReorderTasks -> {
                if (taskOrder.value != event.taskIds) {
                    taskOrder.value = event.taskIds
                    isDayPlanOrderDirty = true
                }
            }
            HomeEvent.CommitDayPlanOrder -> commitDayPlanOrder()
            HomeEvent.OnAddTasksForPlanClicked -> {
                mainState.update {
                    it.copy(
                        isAddTaskForPlanBottomSheetVisible = true,
                        selectedTaskIdsForPlan = emptySet()
                    )
                }
                viewModelScope.launch {
                    val availableTasksForPlan = getAllTasksUseCase()
                        .filterNot {
                            it.isPlannedForToday
                            it.isCompleted
                        }
                        .map { it.toHomeTaskUi() }

                    mainState.update { it.copy(availableTasksForPlan = availableTasksForPlan) }
                }
            }
            is HomeEvent.ToggleTaskSelectionForPlan -> {
                mainState.update { state ->
                    val selectedIds = mainState.value.selectedTaskIdsForPlan
                    state.copy(
                        selectedTaskIdsForPlan = if (event.taskId in selectedIds) {
                            selectedIds - event.taskId
                        } else {
                            selectedIds + event.taskId
                        }
                    )
                }
            }
            is HomeEvent.OnAddTasksForPlanSaved -> addTasksForPlan(event.taskIds)
            HomeEvent.OnCreateTaskClicked -> {
                mainState.update { it.copy(isCreateTaskBottomSheetVisible = true) }
            }
            HomeEvent.OnCreateNoteClicked -> emitEffect(HomeEffect.NavigateToCreateNote)
            HomeEvent.OnCreateTagClicked -> emitEffect(HomeEffect.NavigateToTags)
            HomeEvent.OnOpenFullPlanClicked -> emitEffect(HomeEffect.NavigateToFullPlan)
            is HomeEvent.OnTaskTextChanged -> {
                taskState.update { it.copy(title = event.title) }
            }
            is HomeEvent.OnTaskPlanForTodayChanged -> taskState.update {
                it.copy(isPlannedForToday = event.enabled)
            }
            is HomeEvent.OnTaskTagAdded -> taskState.update { state ->
                state.copy(tags = state.tags + event.tag)
            }
            is HomeEvent.OnTaskTagRemoved -> taskState.update { state ->
                state.copy(tags = state.tags - event.tag)
            }
            HomeEvent.OnBottomSheetDismissed -> {
                if (mainState.value.isCreateTaskBottomSheetVisible) {
                    mainState.update { it.copy(isCreateTaskBottomSheetVisible = false) }
                    taskState.update { TaskState() }
                }
                if (mainState.value.isAddTaskForPlanBottomSheetVisible) {
                    mainState.update { it.copy(isAddTaskForPlanBottomSheetVisible = false) }
                }
            }
            HomeEvent.OnSaveTaskClicked -> {
                saveTask()
            }
        }
    }

    private fun commitDayPlanOrder() {
        if (!isDayPlanOrderDirty) return

        viewModelScope.launch {
            dayPlanOrderCommitMutex.withLock {
                while (isDayPlanOrderDirty) {
                    val orderToSave = taskOrder.value
                    isDayPlanOrderDirty = false
                    try {
                        updateDayPlanOrderUseCase(orderToSave)
                    } catch (_: Exception) {
                        isDayPlanOrderDirty = true
                        return@withLock
                    }

                    if (taskOrder.value != orderToSave) {
                        isDayPlanOrderDirty = true
                    }
                }
            }
        }
    }

    private fun updateTaskStatus(task: HomeTaskUi) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                isCompleted = !task.isCompleted
            )
            updateTaskStatusUseCase(updatedTask.toDomain())
        }
    }

    private fun addTasksForPlan(taskIds: Set<Int>) {
        viewModelScope.launch {
            taskIds.forEach { taskId ->
                val task = getTaskByIdUseCase(taskId)
                task?.let {
                    val updatedTask = it.copy(isPlannedForToday = true)
                    updateTaskStatusUseCase(updatedTask)
                }
            }
        }
        mainState.update { it.copy(isAddTaskForPlanBottomSheetVisible = false) }
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

        onEvent(HomeEvent.OnBottomSheetDismissed)
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }

    private fun dateLabel(): String {
        val locale = Locale.forLanguageTag("ru")
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
            .withLocale(locale)
        return LocalDate.now()
            .format(formatter)
            .replaceFirstChar { it.titlecase(locale) }
    }
}
