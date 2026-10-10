package com.jacqulin.taskmanager.core.data.repository

interface AudioRecorderRepository {
    suspend fun start()
    suspend fun stop(): ByteArray
    suspend fun cancel()
}
