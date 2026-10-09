package com.jacqulin.taskmanager.core.storage.audio

interface AudioRecorder {
    fun start()
    suspend fun stop(): ByteArray
    fun cancel()
}
