package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.data.domain.AppSettingsRepository
import com.jacqulin.taskmanager.data.model.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = appSettingsRepository.appSettings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettings()
        )

    fun onColorPaletteChanged(palette: ColorPalette) {
        viewModelScope.launch {
            appSettingsRepository.setColorPalette(palette)
        }
    }

    fun onDarkThemeConfigChanged(config: DarkThemeConfig) {
        viewModelScope.launch {
            appSettingsRepository.setDarkThemeConfig(config)
        }
    }

    fun resetSettings() {
        viewModelScope.launch {
            appSettingsRepository.setColorPalette(ColorPalette.DEFAULT)
            appSettingsRepository.setDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM)
        }
    }
}
