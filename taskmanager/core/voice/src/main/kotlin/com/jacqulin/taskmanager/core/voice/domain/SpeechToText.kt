package com.jacqulin.taskmanager.core.voice.domain

interface SpeechToText {

    suspend fun recognize(audio: ByteArray): Result<String>
}
