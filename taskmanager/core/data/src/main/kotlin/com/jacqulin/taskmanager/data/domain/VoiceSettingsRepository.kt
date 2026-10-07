package com.jacqulin.taskmanager.data.domain

import com.jacqulin.taskmanager.data.model.VoiceSettings

interface VoiceSettingsRepository {
    suspend fun getVoiceSettings(): VoiceSettings?
    suspend fun setVoiceSettings(providerId: String, credentials: Map<String, String>)
}