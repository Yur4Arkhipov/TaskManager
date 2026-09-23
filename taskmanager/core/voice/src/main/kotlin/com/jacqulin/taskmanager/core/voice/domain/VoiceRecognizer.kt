package com.jacqulin.taskmanager.core.voice.domain

interface VoiceRecognizer {
    fun start()
    suspend fun stopAndRecognize(): Result<String>
    fun cancel()
    val isRecordingActive: Boolean
}
