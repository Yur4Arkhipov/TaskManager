package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.settings.CheckSpeechRecognizerAvailabilityUseCase
import com.jacqulin.taskmanager.core.domain.settings.ObserveUserDataUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetColorPaletteUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetDarkThemeConfigUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetVoiceInputModeUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetYandexTokensUseCase
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import com.jacqulin.taskmanager.feature.settings.presentation.model.UserEditableSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    private val setYandexTokensUseCase: SetYandexTokensUseCase,
    checkSpeechRecognizerAvailabilityUseCase: CheckSpeechRecognizerAvailabilityUseCase,
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> =
        combine(
            observeUserDataUseCase(),
            checkSpeechRecognizerAvailabilityUseCase.isAvailable,
        ) { userData, speechRecognizerAvailable ->
            SettingsUiState.Success(
                settings = UserEditableSettings(
                    colorPalette = userData.colorPalette,
                    darkThemeConfig = userData.darkThemeConfig,
                    voiceInputMode = userData.voiceInputMode,
                ),
                speechRecognizerAvailable = speechRecognizerAvailable,
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

    fun onYandexTokensChanged(iamToken: String, folderId: String) {
        viewModelScope.launch {
            setYandexTokensUseCase(iamToken, folderId)
        }
    }
}
