package com.jacqulin.taskmanager.ui

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.data.model.AppSettings

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(val settings: AppSettings) : MainActivityUiState {
        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean =
            when (settings.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }
    }

    fun getColorPalette(): ColorPalette = when (this) {
        is Success -> this.settings.colorPalette
        is Loading -> ColorPalette.CYAN
    }

    fun shouldKeepSplashScreen(): Boolean = this is Loading
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean = isSystemDarkTheme
}
