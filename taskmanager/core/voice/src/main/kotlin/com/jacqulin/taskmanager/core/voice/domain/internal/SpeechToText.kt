package com.jacqulin.taskmanager.core.voice.domain.internal

internal interface SpeechToText {

    suspend fun recognize(audio: ByteArray): Result<String>
}