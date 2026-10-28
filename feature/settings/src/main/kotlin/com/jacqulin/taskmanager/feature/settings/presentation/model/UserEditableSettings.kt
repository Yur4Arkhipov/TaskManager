package com.jacqulin.taskmanager.feature.settings.presentation.model

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.VoiceInputMode

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val colorPalette: ColorPalette,
    val darkThemeConfig: DarkThemeConfig,
    val voiceInputMode: VoiceInputMode,
)
