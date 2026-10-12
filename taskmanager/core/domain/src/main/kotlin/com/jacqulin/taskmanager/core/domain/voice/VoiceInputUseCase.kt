package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.core.data.repository.VoiceInputRepository
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Unified API for voice capture, independent of the configured recognition strategy.
 */
class VoiceInputUseCase @Inject constructor(
    private val repository: VoiceInputRepository,
) {
    fun listen(): Flow<SpeechRecognitionEvent> = repository.listen()
    suspend fun start(): Result<Unit> = repository.start()
    suspend fun stop(): Result<String> = repository.stop()
    suspend fun cancel() = repository.cancel()
}
