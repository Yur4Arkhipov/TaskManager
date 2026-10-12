package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.settings.ObserveUserDataUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetColorPaletteUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetDarkThemeConfigUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetVoiceInputModeUseCase
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    observeUserDataUseCase: ObserveUserDataUseCase,
    private val setColorPaletteUseCase: SetColorPaletteUseCase,
    private val setDarkThemeConfigUseCase: SetDarkThemeConfigUseCase,
    private val setVoiceInputModeUseCase: SetVoiceInputModeUseCase,
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        observeUserDataUseCase()
            .map { userData ->
                SettingsUiState.Success(
                    settings = UserEditableSettings(
                        colorPalette =  userData.colorPalette,
                        darkThemeConfig = userData.darkThemeConfig,
                        voiceInputMode = userData.voiceInputMode,
                    ),
                )
            }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = SettingsUiState.Loading
        )

    fun onColorPaletteChanged(palette: ColorPalette) {
        viewModelScope.launch {
            setColorPaletteUseCase(palette)
        }
    }

    fun onDarkThemeConfigChanged(config: DarkThemeConfig) {
        viewModelScope.launch {
            setDarkThemeConfigUseCase(config)
        }
    }

    fun onVoiceInputModeChanged(mode: VoiceInputMode) {
        viewModelScope.launch { setVoiceInputModeUseCase(mode) }
    }
}

/**
 * Represents the settings which the user can edit within the app.
 */
data class UserEditableSettings(
    val colorPalette: ColorPalette,
    val darkThemeConfig: DarkThemeConfig,
    val voiceInputMode: VoiceInputMode,
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}
