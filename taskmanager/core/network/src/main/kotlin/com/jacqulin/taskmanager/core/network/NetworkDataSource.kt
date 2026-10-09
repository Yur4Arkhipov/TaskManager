package com.jacqulin.taskmanager.core.network

interface NetworkDataSource {
    suspend fun recognize(
        audio: ByteArray,
        folderId: String,
        iamToken: String,
    ): Result<String>
}
