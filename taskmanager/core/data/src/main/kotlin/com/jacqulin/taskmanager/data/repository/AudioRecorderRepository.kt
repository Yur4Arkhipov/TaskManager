package com.jacqulin.taskmanager.data.repository

interface AudioRecorderRepository {
    fun start()
    suspend fun stop(): ByteArray
    fun cancel()
}
