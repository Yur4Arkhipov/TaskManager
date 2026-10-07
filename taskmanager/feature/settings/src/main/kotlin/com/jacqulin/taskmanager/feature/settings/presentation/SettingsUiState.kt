package com.jacqulin.taskmanager.feature.settings.presentation

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.voice.data.model.SpeechField

data class SpeechFieldUiState(
    val field: SpeechField,
    val value: String,
    val isSaved: Boolean,
)

data class SettingsUiState(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val colorPalette: ColorPalette = ColorPalette.CYAN,
    val voiceLoaded: Boolean = false,
    val selectedProvider: String? = null,
    val fields: List<SpeechFieldUiState> = emptyList(),
    val canSave: Boolean = false,
    val isSaving: Boolean = false,
)
