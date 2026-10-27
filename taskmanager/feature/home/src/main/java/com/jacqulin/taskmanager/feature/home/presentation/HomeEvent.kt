package com.jacqulin.taskmanager.feature.home.presentation

import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi

sealed interface HomeEvent {
    data class OnTaskRemovedFromDayPlan(val task: HomeTaskUi) : HomeEvent
    data class UpdateTaskStatus(val task: HomeTaskUi) : HomeEvent
    data object OnAddTaskClicked : HomeEvent
    data object OnStartFocusClicked : HomeEvent
    data object OnShowFullPlanClicked : HomeEvent
    data object OnCreateNoteClicked : HomeEvent
    data object OnCreateTaskClicked : HomeEvent
    data object OnBreakDownThoughtClicked : HomeEvent
}
