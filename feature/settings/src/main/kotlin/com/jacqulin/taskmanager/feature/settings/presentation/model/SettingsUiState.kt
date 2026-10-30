package com.jacqulin.taskmanager.feature.settings.presentation.model

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(
        val settings: UserEditableSettings,
        val speechRecognizerAvailable: Boolean = true,
        val areYandexTokensConfigured: Boolean = false,
    ) : SettingsUiState
}
