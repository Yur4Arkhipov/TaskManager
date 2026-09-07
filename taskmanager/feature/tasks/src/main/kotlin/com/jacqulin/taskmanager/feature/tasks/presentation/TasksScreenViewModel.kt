package com.jacqulin.taskmanager.feature.tasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.tasks.domain.model.Task
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.DeleteTaskUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.ObserveTasksUseCase
import com.jacqulin.taskmanager.feature.tasks.domain.usecase.SaveTaskUseCase
import com.jacqulin.taskmanager.feature.tasks.presentation.mapper.toUiModel
import com.jacqulin.taskmanager.feature.tasks.presentation.model.DraftTaskUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksScreenViewModel @Inject constructor(
    observeTasksUseCase: ObserveTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel() {

    private val searchQueryInput = MutableStateFlow("")
    private val appliedSearchQuery = MutableStateFlow("")
    private val sortType = MutableStateFlow(SortType.NEW_TO_OLD)

    private val _draftTask = MutableStateFlow<DraftTaskUi?>(null)

    val uiState: StateFlow<TasksUiState> =
        combine(
            observeTasksUseCase(),
            searchQueryInput,
            appliedSearchQuery,
            sortType,
            _draftTask
        ) { tasks, inputQuery, appliedQuery, sortType, draftTask  ->

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
                draftTask = draftTask,
            )
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
                // позже
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
}