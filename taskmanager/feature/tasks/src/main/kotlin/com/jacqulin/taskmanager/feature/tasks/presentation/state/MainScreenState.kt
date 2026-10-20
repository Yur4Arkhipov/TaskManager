package com.jacqulin.taskmanager.feature.tasks.presentation.state

import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter

data class MainScreenState(
    val searchQuery: String = "",
    val sortType: SortType = SortType.NEW_TO_OLD,
    val filter: TaskFilter = TaskFilter.ALL,
    val isBottomSheetVisible: Boolean = false,
)
