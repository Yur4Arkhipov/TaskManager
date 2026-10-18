package com.jacqulin.taskmanager.feature.settings.presentation

import com.jacqulin.taskmanager.feature.settings.presentation.model.UserEditableSettings

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}
