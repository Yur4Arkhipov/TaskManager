package com.jacqulin.taskmanager.ui

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.UserData

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState

    data class Success(val userData: UserData) : MainActivityUiState {
        override fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean =
            when (userData.darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> isSystemDarkTheme
                DarkThemeConfig.LIGHT -> false
                DarkThemeConfig.DARK -> true
            }
    }

    fun getColorPalette(): ColorPalette = when (this) {
        is Success -> this.userData.colorPalette
        is Loading -> ColorPalette.DEFAULT
    }

    fun shouldKeepSplashScreen(): Boolean = this is Loading
    fun shouldUseDarkTheme(isSystemDarkTheme: Boolean): Boolean = isSystemDarkTheme
}
