package com.jacqulin.taskmanager.core.voice.domain

interface SpeechProvider {
    val id: String
    suspend fun recognize(credentials: Map<String, String>, audio: ByteArray): Result<String>
}