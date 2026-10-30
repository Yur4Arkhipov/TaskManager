package com.jacqulin.taskmanager.feature.tasks.presentation.model

import com.jacqulin.taskmanager.core.designsystem.model.SortType

data class MainScreenState(
    val searchQuery: String = "",
    val sortType: SortType = SortType.NEW_TO_OLD,
    val filter: TaskFilter = TaskFilter.ALL,
    val isBottomSheetVisible: Boolean = false,
    val isDeleteModeEnabled: Boolean = false,
    val selectedTaskIds: Set<Int> = emptySet(),
    val showCreationDate: Boolean = true,
    val showTags: Boolean = true,
    val isCompactView: Boolean = false,
)
