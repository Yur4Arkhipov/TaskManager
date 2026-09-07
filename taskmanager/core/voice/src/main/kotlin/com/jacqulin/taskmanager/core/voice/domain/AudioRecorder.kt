package com.jacqulin.taskmanager.core.voice.domain

interface AudioRecorder {
    fun start()
    suspend fun stop(): ByteArray
    fun cancel()
}
