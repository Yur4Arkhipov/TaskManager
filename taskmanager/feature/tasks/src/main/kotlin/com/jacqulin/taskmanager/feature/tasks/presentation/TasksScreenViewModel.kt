package com.jacqulin.taskmanager.feature.tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.ObserveTasksUseCase
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TasksScreenViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
//    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val searchQueryInput = MutableStateFlow("")
    private val appliedSearchQuery = MutableStateFlow("")
    private val sortType = MutableStateFlow(SortType.NEW_TO_OLD)

//    private val _effects = MutableSharedFlow<TasksEffect>()
//    val effects: SharedFlow<TasksEffect> = _effects.asSharedFlow()

    val uiState: StateFlow<TasksUiState> =
        combine(
            observeTasksUseCase(),
            searchQueryInput,
            appliedSearchQuery,
            sortType
        ) { tasks, inputQuery, appliedQuery, sortType  ->

            val visibleTasks = tasks
                .map { it.toUiModel() }
                .filter { task ->
                    val query = appliedQuery.trim()
                    query.isBlank() ||
                        task.title.contains(query, ignoreCase = true)
                }
                .let { tasks ->
                    when (sortType) {
                        SortType.NEW_TO_OLD ->
                            tasks.sortedByDescending { it.createdAtMillis }
                        SortType.OLD_TO_NEW ->
                            tasks.sortedBy { it.createdAtMillis }
                    }
                }

            TasksUiState(
                searchQueryInput = inputQuery,
                appliedSearchQuery = appliedQuery,
                sortType = sortType,
                visibleTasks = visibleTasks,
                isEmpty = visibleTasks.isEmpty(),
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            TasksUiState()
        )
}