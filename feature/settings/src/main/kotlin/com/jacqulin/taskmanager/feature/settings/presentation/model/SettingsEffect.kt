package com.jacqulin.taskmanager.feature.settings.presentation.model

sealed interface SettingsEffect {
    data class ShowSuccess(val message: String) : SettingsEffect
    data class ShowError(val message: String) : SettingsEffect
}
