package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.feature.settings.di.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * User actions for the settings screen.
 */
sealed interface SettingsEvent {
    data class OnThemeChanged(val theme: ThemePreference) : SettingsEvent
    data class OnColorSchemeChanged(val colorScheme: ColorScheme) : SettingsEvent
    data object OnResetSettingsClicked : SettingsEvent
}

/**
 * ViewModel for the settings screen.
 */
@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsManager.themePreference,
        settingsManager.colorScheme
    ) { theme, colorScheme ->
        SettingsUiState(
            themePreference = theme,
            colorScheme = colorScheme
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnThemeChanged -> {
                viewModelScope.launch {
                    settingsManager.setThemePreference(event.theme)
                }
            }

            is SettingsEvent.OnColorSchemeChanged -> {
                viewModelScope.launch {
                    settingsManager.setColorScheme(event.colorScheme)
                }
            }

            is SettingsEvent.OnResetSettingsClicked -> {
                viewModelScope.launch {
                    settingsManager.resetSettings()
                }
            }
        }
    }
}
