package com.jacqulin.taskmanager.feature.home.presentation.model

import com.jacqulin.taskmanager.core.model.Tag

sealed interface HomeEvent {
    data class OnTaskRemovedFromDayPlan(val task: HomeTaskUi) : HomeEvent
    data class UpdateTaskStatus(val task: HomeTaskUi) : HomeEvent
    data class ReorderTasks(val taskIds: List<Int>) : HomeEvent
    data class OnTaskTextChanged(val title: String) : HomeEvent
    data class OnTaskPlanForTodayChanged(val enabled: Boolean) : HomeEvent
    data class OnTaskTagAdded(val tag: Tag) : HomeEvent
    data class OnTaskTagRemoved(val tag: Tag) : HomeEvent
    data object CommitDayPlanOrder : HomeEvent
    data object OnAddTasksForPlanClicked : HomeEvent
    data class OnAddTasksForPlanSaved(val taskIds: Set<Int>) : HomeEvent
    data class ToggleTaskSelectionForPlan(val taskId: Int) : HomeEvent
    data object OnCreateNoteClicked : HomeEvent
    data object OnCreateTaskClicked : HomeEvent
    data object OnOpenFullPlanClicked: HomeEvent
    data object OnCreateTagClicked: HomeEvent
    data object OnBottomSheetDismissed : HomeEvent
    data object OnSaveTaskClicked : HomeEvent
}
