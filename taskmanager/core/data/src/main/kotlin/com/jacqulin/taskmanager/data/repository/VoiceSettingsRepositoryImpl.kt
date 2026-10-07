package com.jacqulin.taskmanager.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jacqulin.taskmanager.core.voice.data.model.SpeechProviderCatalog
import com.jacqulin.taskmanager.data.domain.VoiceSettingsRepository
import com.jacqulin.taskmanager.data.model.VoiceSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.voiceDataStore by preferencesDataStore(name = "voice_settings")

@Singleton
class VoiceSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : VoiceSettingsRepository {

    private object PreferencesKeys {
        val VOICE_PROVIDER_KEY = stringPreferencesKey("voice_provider_key")
        fun credential(key: String) = stringPreferencesKey("credential_$key")
    }

    override suspend fun getVoiceSettings(): VoiceSettings? {
        val prefs = context.voiceDataStore.data.first()
        val providerId = prefs[PreferencesKeys.VOICE_PROVIDER_KEY] ?: return null
        val provider = SpeechProviderCatalog.byId(providerId) ?: return null

        val credentials = provider.fields
            .mapNotNull { field ->
                prefs[PreferencesKeys.credential(field.key)]?.let { field.key to it }
            }
            .toMap()

        return VoiceSettings(providerId, credentials)
    }

    override suspend fun setVoiceSettings(providerId: String, credentials: Map<String, String>) {
        context.voiceDataStore.edit { prefs ->
            prefs[PreferencesKeys.VOICE_PROVIDER_KEY] = providerId

            prefs.asMap().keys
                .filterIsInstance<Preferences.Key<String>>()
                .filter { it.name.startsWith("credential_") }
                .forEach { prefs.remove(it) }

            credentials.forEach { (k, v) -> prefs[PreferencesKeys.credential(k)] = v }
        }
    }
}