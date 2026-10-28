package com.jacqulin.taskmanager.feature.home.presentation.model

sealed interface HomeEffect {
    data object NavigateToCreateTask : HomeEffect
    data object NavigateToCreateNote : HomeEffect
}
