package com.jacqulin.taskmanager.feature.tasks.presentation

import com.jacqulin.taskmanager.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi

sealed interface TasksEvent {
    data class OnSearchQueryChanged(val query: String) : TasksEvent
    data object OnSearchSubmitted : TasksEvent
    data class OnSortChanged(val sortType: SortType) : TasksEvent
    data class OnDeleteTaskClicked(val taskId: Int) : TasksEvent
    data object OnCreateTaskByTextClicked : TasksEvent
    data object OnCreateTaskByVoiceClicked : TasksEvent
    data class OnDraftTaskTextChanged(val text: String) : TasksEvent
    data object OnDraftTaskSaveClicked : TasksEvent
    data object OnDraftTaskDeleteClicked : TasksEvent
    data class UpdateTaskStatus(val task: TaskItemUi) : TasksEvent
}
