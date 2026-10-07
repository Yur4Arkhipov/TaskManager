package com.jacqulin.taskmanager.feature.settings.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.voice.data.model.SpeechProviderCatalog
import com.jacqulin.taskmanager.data.domain.AppSettingsRepository
import com.jacqulin.taskmanager.data.domain.VoiceSettingsRepository
import com.jacqulin.taskmanager.data.model.AppSettings
import com.jacqulin.taskmanager.data.model.VoiceSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
    private val voiceSettingsRepository: VoiceSettingsRepository
) : ViewModel() {

    private val persistedVoice = MutableStateFlow<VoiceSettings?>(null)
    private val voiceLoaded = MutableStateFlow(false)
    private val providerDraft = MutableStateFlow<String?>(null)
    private val credentialsDraft = MutableStateFlow<Map<String, String>?>(null)
    private val isSaving = MutableStateFlow(false)
    private val _tokenSavedEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val tokenSavedEvent: SharedFlow<String> = _tokenSavedEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            persistedVoice.value = voiceSettingsRepository.getVoiceSettings()
            voiceLoaded.value = true
        }
    }

    val uiState: StateFlow<SettingsUiState> = combine(
        appSettingsRepository.appSettings,
        persistedVoice,
        voiceLoaded,
        providerDraft,
        credentialsDraft,
        isSaving,
    ) { values ->
        val appSettings   = values[0] as AppSettings
        val persisted     = values[1] as VoiceSettings?
        val loaded        = values[2] as Boolean
        val providerDraft = values[3] as String?
        val credsDraft    = values[4] as Map<String, String>?
        val saving        = values[5] as Boolean
        val provider = providerDraft ?: persisted?.providerId
        val schema = provider?.let(SpeechProviderCatalog::byId)?.fields.orEmpty()

        val effective = schema.associate { field ->
            val v = credsDraft?.get(field.key)
                ?: persisted?.credentials?.get(field.key).orEmpty()
            field.key to v
        }

        val fieldsUi = schema.map { field ->
            val draftValue = credsDraft?.get(field.key)
            val persistedValue = persisted?.credentials?.get(field.key).orEmpty()
            val display = when {
                draftValue != null -> draftValue
                field.isSecret      -> ""
                else                -> persistedValue
            }
            SpeechFieldUiState(
                field = field,
                value = display,
                isSaved = persistedValue.isNotBlank(),
            )
        }

        val providerChanged = providerDraft != null && providerDraft != persisted?.providerId
        val credsChanged = credsDraft != null && schema.any { field ->
            (credsDraft[field.key].orEmpty()) != persisted?.credentials?.get(field.key).orEmpty()
        }
        val allFilled = schema.isNotEmpty() && effective.values.none { it.isBlank() }
        val canSave = loaded && !saving && provider != null && allFilled &&
                (providerChanged || credsChanged || persisted == null)

        SettingsUiState(
            darkThemeConfig = appSettings.darkThemeConfig,
            colorPalette = appSettings.colorPalette,
            voiceLoaded = loaded,
            selectedProvider = provider,
            fields = fieldsUi,
            canSave = canSave,
            isSaving = saving,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(),
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

    fun onVoiceProviderChanged(id: String) {
        if (id == (providerDraft.value ?: persistedVoice.value?.providerId)) {
            providerDraft.value = null
            credentialsDraft.value = null
        } else {
            providerDraft.value = id
            credentialsDraft.value = emptyMap()
        }
    }

    fun onCredentialChanged(key: String, value: String) {
        val current = credentialsDraft.value
            ?: persistedVoice.value?.credentials.orEmpty()
        credentialsDraft.value = current + (key to value)
    }

    fun onSaveVoiceSettingsClicked() {
        val provider = providerDraft.value ?: persistedVoice.value?.providerId ?: return
        val schema = SpeechProviderCatalog.byId(provider)?.fields.orEmpty()

        val merged = buildMap {
            schema.forEach { field ->
                val v = credentialsDraft.value?.get(field.key)
                    ?: persistedVoice.value?.credentials?.get(field.key)
                if (!v.isNullOrBlank()) put(field.key, v)
            }
        }
        if (schema.any { merged[it.key].isNullOrBlank() }) return

        viewModelScope.launch {
            isSaving.value = true
            try {
                voiceSettingsRepository.setVoiceSettings(provider, merged)
                persistedVoice.value = VoiceSettings(provider, merged)
                providerDraft.value = null
                credentialsDraft.value = null

                _tokenSavedEvent.tryEmit(
                    SpeechProviderCatalog.byId(provider)?.name ?: provider
                )
            } finally {
                isSaving.value = false
            }
        }
    }

//    fun resetSettings() {
//        viewModelScope.launch {
//            appSettingsRepository.setColorPalette(ColorPalette.CYAN)
//            appSettingsRepository.setDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM)
//        }
//    }
}
