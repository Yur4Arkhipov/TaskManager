package com.jacqulin.taskmanager.core.network

import com.jacqulin.taskmanager.core.model.AppLanguage

interface NetworkDataSource {
    suspend fun recognize(
        audio: ByteArray,
        folderId: String,
        iamToken: String,
        language: AppLanguage
    ): Result<String>
}
