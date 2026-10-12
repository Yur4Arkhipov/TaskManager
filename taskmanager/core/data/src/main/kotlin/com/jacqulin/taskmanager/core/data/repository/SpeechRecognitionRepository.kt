package com.jacqulin.taskmanager.core.data.repository

internal interface SpeechRecognitionRepository {
    suspend fun recognize(audio: ByteArray): Result<String>
}
