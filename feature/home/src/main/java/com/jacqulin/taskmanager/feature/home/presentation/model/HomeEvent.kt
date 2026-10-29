package com.jacqulin.taskmanager.feature.home.presentation.model

sealed interface HomeEvent {
    data class OnTaskRemovedFromDayPlan(val task: HomeTaskUi) : HomeEvent
    data class UpdateTaskStatus(val task: HomeTaskUi) : HomeEvent
    data class ReorderTasks(val taskIds: List<Int>) : HomeEvent
    data object CommitDayPlanOrder : HomeEvent
    data object OnAddTaskClicked : HomeEvent
    data object OnCreateNoteClicked : HomeEvent
    data object OnCreateTaskClicked : HomeEvent
}
