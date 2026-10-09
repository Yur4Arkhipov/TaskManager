package com.jacqulin.taskmanager.data.repository

interface SpeechRecognitionRepository {
    suspend fun recognize(audio: ByteArray): Result<String>
}