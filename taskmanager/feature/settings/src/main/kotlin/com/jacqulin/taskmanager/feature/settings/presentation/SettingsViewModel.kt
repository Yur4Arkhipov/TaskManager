package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.domain.settings.AreYandexTokensConfiguredUseCase
import com.jacqulin.taskmanager.core.domain.settings.CheckSpeechRecognizerAvailabilityUseCase
import com.jacqulin.taskmanager.core.domain.settings.ObserveUserDataUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetColorPaletteUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetDarkThemeConfigUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetVoiceInputModeUseCase
import com.jacqulin.taskmanager.core.domain.settings.SetYandexTokensUseCase
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import com.jacqulin.taskmanager.feature.settings.presentation.model.SettingsEffect
import com.jacqulin.taskmanager.feature.settings.presentation.model.UserEditableSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val areYandexTokensConfiguredUseCase: AreYandexTokensConfiguredUseCase,
    checkSpeechRecognizerAvailabilityUseCase: CheckSpeechRecognizerAvailabilityUseCase,
) : ViewModel() {

    private val _effects = MutableSharedFlow<SettingsEffect>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effects: SharedFlow<SettingsEffect> = _effects.asSharedFlow()

    private val yandexTokensConfigured = MutableStateFlow<Boolean?>(null)

    init {
        viewModelScope.launch {
            yandexTokensConfigured.value = runCatching {
                areYandexTokensConfiguredUseCase()
            }.getOrDefault(false)
        }
    }

    val settingsUiState: StateFlow<SettingsUiState> =
        combine(
            observeUserDataUseCase(),
            checkSpeechRecognizerAvailabilityUseCase.isAvailable,
            yandexTokensConfigured,
        ) { userData, speechRecognizerAvailable, areTokensConfigured ->
            if (areTokensConfigured == null) return@combine SettingsUiState.Loading
            SettingsUiState.Success(
                settings = UserEditableSettings(
                    colorPalette = userData.colorPalette,
                    darkThemeConfig = userData.darkThemeConfig,
                    voiceInputMode = userData.voiceInputMode,
                ),
                speechRecognizerAvailable = speechRecognizerAvailable,
                areYandexTokensConfigured = areTokensConfigured,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            initialValue = SettingsUiState.Loading
        )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnColorPaletteChanged -> onColorPaletteChanged(event.palette)
            is SettingsEvent.OnDarkThemeConfigChanged -> onDarkThemeConfigChanged(event.config)
            is SettingsEvent.OnVoiceInputModeChanged -> onVoiceInputModeChanged(event.mode)
            is SettingsEvent.OnYandexTokensChanged -> onYandexTokensChanged(event.iamToken, event.folderId)
        }
    }

    private fun onColorPaletteChanged(palette: ColorPalette) {
        viewModelScope.launch {
            setColorPaletteUseCase(palette)
        }
    }

    private fun onDarkThemeConfigChanged(config: DarkThemeConfig) {
        viewModelScope.launch {
            setDarkThemeConfigUseCase(config)
        }
    }

    private fun onVoiceInputModeChanged(mode: VoiceInputMode) {
        viewModelScope.launch { setVoiceInputModeUseCase(mode) }
    }

    private fun onYandexTokensChanged(iamToken: String, folderId: String) {
        viewModelScope.launch {
            runCatching {
                setYandexTokensUseCase(iamToken, folderId)
            }.onSuccess {
                yandexTokensConfigured.value = true
                _effects.emit(SettingsEffect.ShowSuccess("Данные для Yandex сохранены"))
            }.onFailure { e ->
                _effects.emit(SettingsEffect.ShowError(e.message ?: "Не удалось сохранить токены"))
            }
        }
    }
}
