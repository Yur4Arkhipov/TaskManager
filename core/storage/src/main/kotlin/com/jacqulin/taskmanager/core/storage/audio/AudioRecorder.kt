package com.jacqulin.taskmanager.core.storage.audio

interface AudioRecorder {
    suspend fun start()
    suspend fun stop(): ByteArray
    suspend fun cancel()
}
