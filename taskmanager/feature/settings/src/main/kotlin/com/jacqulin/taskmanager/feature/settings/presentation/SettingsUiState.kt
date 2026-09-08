package com.jacqulin.taskmanager.feature.settings.presentation

/**
 * UI state for the settings screen.
 */
data class SettingsUiState(
    val themePreference: ThemePreference = ThemePreference.System,
    val colorScheme: ColorScheme = ColorScheme.PURPLE,
    val isResetting: Boolean = false
)
