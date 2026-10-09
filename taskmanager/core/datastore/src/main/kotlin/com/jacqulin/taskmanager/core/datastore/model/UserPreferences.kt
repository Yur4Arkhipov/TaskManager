package com.jacqulin.taskmanager.core.datastore.model

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig

data class UserPreferences(
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val colorPalette: ColorPalette = ColorPalette.CYAN
)
