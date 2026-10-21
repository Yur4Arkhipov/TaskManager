package com.jacqulin.taskmanager.feature.home.presentation

sealed interface HomeEvent {
    data class OnTaskDeleted(val taskId: Int) : HomeEvent
    data object OnAddTaskClicked : HomeEvent
    data object OnAddNoteClicked : HomeEvent
    data object OnStartFocusClicked : HomeEvent
    data object OnShowFullPlanClicked : HomeEvent
    data object OnAddToPlanClicked : HomeEvent
    data object OnBreakDownThoughtClicked : HomeEvent
}
