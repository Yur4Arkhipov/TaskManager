package com.jacqulin.taskmanager.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.core.domain.tasks.UpdateDayPlanOrderUseCase
import com.jacqulin.taskmanager.core.domain.tasks.UpdateTaskStatusUseCase
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.model.toDomain
import com.jacqulin.taskmanager.feature.home.presentation.state.MainState
import com.jacqulin.taskmanager.feature.home.presentation.state.TaskState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val updateDayPlanOrderUseCase: UpdateDayPlanOrderUseCase,
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
            mainState,
            taskState,
            taskOrder,
        ) { tasks, mainState, taskState, taskOrder ->
            HomeUiState(
                mainState = mainState.copy(
                    dateLabel = dateLabel(),
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
            HomeEvent.OnAddTaskClicked,
            HomeEvent.OnCreateTaskClicked -> { emitEffect(HomeEffect.NavigateToCreateTask) }
            HomeEvent.OnCreateNoteClicked -> emitEffect(HomeEffect.NavigateToCreateNote)
            HomeEvent.OnShowFullPlanClicked -> Unit
            HomeEvent.OnBreakDownThoughtClicked -> Unit
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

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }

    private fun dateLabel(): String {
        val locale = Locale.forLanguageTag("ru")
        val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(locale)
        return LocalDate.now()
            .format(formatter)
            .replaceFirstChar { it.titlecase(locale) }
    }
}
