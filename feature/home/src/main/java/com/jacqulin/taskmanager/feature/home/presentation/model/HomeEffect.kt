package com.jacqulin.taskmanager.feature.home.presentation.model

sealed interface HomeEffect {
    data object NavigateToTags : HomeEffect
    data object NavigateToCreateNote : HomeEffect
}
