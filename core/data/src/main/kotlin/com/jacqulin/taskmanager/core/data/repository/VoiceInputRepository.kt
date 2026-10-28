package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import kotlinx.coroutines.flow.Flow

/**
 * A single voice-input contract for presentation and domain layers.
 */
interface VoiceInputRepository {
    fun listen(): Flow<SpeechRecognitionEvent>
    suspend fun start(): Result<Unit>
    suspend fun stop(): Result<String>
    suspend fun cancel()
}
