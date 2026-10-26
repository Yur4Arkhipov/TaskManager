package com.jacqulin.taskmanager.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.tasks.ObserveTasksUseCase
import com.jacqulin.taskmanager.core.domain.tasks.UpdateTaskStatusUseCase
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.model.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
) : ViewModel() {

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects = _effects.asSharedFlow()

    val uiState: StateFlow<HomeUiState> = observeTasksUseCase()
        .map { tasks ->
            HomeUiState(
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
                    )
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(dateLabel = dateLabel()),
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
                    )
                    updateTaskStatusUseCase(task)
                }
            }
            is HomeEvent.UpdateTaskStatus -> {
                updateTaskStatus(event.task)
            }
            HomeEvent.OnAddTaskClicked,
            HomeEvent.OnAddToPlanClicked -> emitEffect(HomeEffect.NavigateToCreateTask)
            HomeEvent.OnAddNoteClicked -> emitEffect(HomeEffect.NavigateToCreateNote)
            HomeEvent.OnStartFocusClicked -> Unit
            HomeEvent.OnShowFullPlanClicked -> Unit
            HomeEvent.OnBreakDownThoughtClicked -> emitEffect(HomeEffect.NavigateToCreateTask)
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

    private fun dateLabel(): String = SimpleDateFormat(
            "EEEE, d MMMM",
            Locale("ru"),
        ).format(Date()).replaceFirstChar { it.uppercase(Locale("ru")) }


//    private companion object {
//        fun dateLabel(): String = SimpleDateFormat(
//            "EEEE, d MMMM",
//            Locale("ru"),
//        ).format(Date()).replaceFirstChar { it.uppercase(Locale("ru")) }
//    }
}
