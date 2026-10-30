package com.jacqulin.taskmanager.feature.settings.presentation.model

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.VoiceInputMode

sealed interface SettingsEvent {
    data class OnColorPaletteChanged(val palette: ColorPalette) : SettingsEvent
    data class OnDarkThemeConfigChanged(val config: DarkThemeConfig) : SettingsEvent
    data class OnVoiceInputModeChanged(val mode: VoiceInputMode) : SettingsEvent
    data class OnYandexTokensChanged(
        val iamToken: String,
        val folderId: String
    ) : SettingsEvent
}
