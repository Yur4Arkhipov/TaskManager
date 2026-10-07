package com.jacqulin.taskmanager.core.voice.domain.internal

internal interface AudioRecorder {
    fun start()
    suspend fun stop(): ByteArray
    fun cancel()
}