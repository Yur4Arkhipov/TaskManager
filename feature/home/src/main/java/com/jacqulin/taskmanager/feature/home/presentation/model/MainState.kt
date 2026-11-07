package com.jacqulin.taskmanager.feature.home.presentation.model

import com.jacqulin.taskmanager.core.model.Tag

data class MainState(
    val tasks: List<HomeTaskUi> = emptyList(),
    val availableTasksForPlan: List<HomeTaskUi> = emptyList(),
    val availableTags: List<Tag> = emptyList(),
    val isCreateTaskBottomSheetVisible: Boolean = false,
    val isAddTaskForPlanBottomSheetVisible: Boolean = false,
    val selectedTaskIdsForPlan: Set<Int> = emptySet(),
)
