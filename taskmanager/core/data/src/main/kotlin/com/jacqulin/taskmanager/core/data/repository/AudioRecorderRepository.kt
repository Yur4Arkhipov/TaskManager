package com.jacqulin.taskmanager.core.data.repository

interface AudioRecorderRepository {
    fun start()
    suspend fun stop(): ByteArray
    fun cancel()
}
