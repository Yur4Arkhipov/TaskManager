package com.jacqulin.taskmanager.data.model

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig

data class AppSettings(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val colorPalette: ColorPalette = ColorPalette.CYAN
)
