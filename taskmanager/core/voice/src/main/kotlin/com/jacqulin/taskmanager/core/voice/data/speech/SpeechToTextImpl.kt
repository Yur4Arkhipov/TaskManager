package com.jacqulin.taskmanager.core.voice.data.speech

import android.util.Log
import com.jacqulin.taskmanager.core.voice.domain.SpeechException
import com.jacqulin.taskmanager.core.voice.domain.SpeechProvider
import com.jacqulin.taskmanager.core.voice.domain.internal.SpeechToText
import com.jacqulin.taskmanager.data.domain.VoiceSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SpeechToTextImpl @Inject constructor(
    private val voiceSettingsRepository: VoiceSettingsRepository,
    private val providers: Set<@JvmSuppressWildcards SpeechProvider>,
) : SpeechToText {

    override suspend fun recognize(
        audio: ByteArray
    ): Result<String> = withContext(Dispatchers.IO) {
        val settings = voiceSettingsRepository.getVoiceSettings()
            ?: return@withContext Result.failure(SpeechException.NotConfigured())

        val provider = providers.firstOrNull { it.id == settings.providerId }
            ?: return@withContext Result.failure(
                SpeechException.ProviderNotImplemented(settings.providerId)
            )

        provider.recognize(settings.credentials, audio)
    }
}
