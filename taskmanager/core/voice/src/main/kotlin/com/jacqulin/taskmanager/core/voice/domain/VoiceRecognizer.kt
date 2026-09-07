package com.jacqulin.taskmanager.core.voice.domain

import kotlinx.coroutines.flow.StateFlow

interface VoiceRecognizer {
    val state: StateFlow<VoiceState>
    fun start()
    suspend fun stop()
    fun cancel()
}
