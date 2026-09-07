package com.jacqulin.taskmanager.feature.tasks.presentation

import com.jacqulin.taskmanager.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.tasks.presentation.model.DraftTaskUi
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi

data class TasksUiState(
    val searchQueryInput: String = "",
    val appliedSearchQuery: String = "",
    val sortType: SortType = SortType.NEW_TO_OLD,
    val visibleTasks: List<TaskItemUi> = emptyList(),
    val isEmpty: Boolean = true,
    val draftTask: DraftTaskUi? = null
)