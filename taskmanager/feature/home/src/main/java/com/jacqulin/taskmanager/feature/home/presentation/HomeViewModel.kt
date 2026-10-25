package com.jacqulin.taskmanager.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            dateLabel = dateLabel(),
            tasks = listOf(
                HomeTaskUi(id = 1, title = "Подготовить отчёт", durationMinutes = 25),
                HomeTaskUi(id = 2, title = "Ответить на письма", durationMinutes = 15),
                HomeTaskUi(id = 3, title = "Ответить на письма", durationMinutes = null),
                HomeTaskUi(id = 4, title = "Ответить на письма", durationMinutes = 15),
                HomeTaskUi(id = 5, title = "Ответить на письма", durationMinutes = 15),
            ),
        ),
    )
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnTaskDeleted -> _uiState.update { state ->
                state.copy(tasks = state.tasks.filterNot { it.id == event.taskId })
            }
            is HomeEvent.OnTaskCompletionChanged -> _uiState.update { state ->
                state.copy(
                    tasks = state.tasks.map { task ->
                        if (task.id == event.taskId) task.copy(isCompleted = event.isCompleted) else task
                    }
                )
            }
            HomeEvent.OnAddTaskClicked,
            HomeEvent.OnAddToPlanClicked -> emitEffect(HomeEffect.NavigateToCreateTask)
            HomeEvent.OnAddNoteClicked -> emitEffect(HomeEffect.NavigateToCreateNote)
            HomeEvent.OnStartFocusClicked -> _uiState.update {
                it.copy(isFocusStarted = !it.isFocusStarted)
            }
            HomeEvent.OnShowFullPlanClicked -> _uiState.update {
                it.copy(isFullPlanShown = !it.isFullPlanShown)
            }
            HomeEvent.OnBreakDownThoughtClicked -> emitEffect(HomeEffect.NavigateToCreateTask)
        }
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch { _effects.emit(effect) }
    }

    private companion object {
        fun dateLabel(): String = SimpleDateFormat(
            "EEEE, d MMMM",
            Locale("ru"),
        ).format(Date()).replaceFirstChar { it.uppercase(Locale("ru")) }
    }
}
