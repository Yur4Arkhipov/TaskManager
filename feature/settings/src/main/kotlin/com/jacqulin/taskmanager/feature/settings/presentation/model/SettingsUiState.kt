package com.jacqulin.taskmanager.feature.settings.presentation.model

import com.jacqulin.taskmanager.core.model.AppLanguage

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(
        val settings: UserEditableSettings,
        val androidSpeechRecognizerAvailable: Boolean = true,
        val yandexSpeechRecognizerAvailable: Boolean = true,
        val areYandexTokensConfigured: Boolean = false,
        val lang: AppLanguage = AppLanguage.ENGLISH
    ) : SettingsUiState
}
