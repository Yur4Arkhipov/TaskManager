package com.jacqulin.taskmanager.feature.home.presentation

sealed interface HomeEffect {
    data object NavigateToCreateTask : HomeEffect
    data object NavigateToCreateNote : HomeEffect
}
