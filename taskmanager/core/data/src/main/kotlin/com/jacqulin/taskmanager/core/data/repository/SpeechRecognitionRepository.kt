package com.jacqulin.taskmanager.core.data.repository

internal interface SpeechRecognitionRepository {
    suspend fun recognizeYandex(
        audio: ByteArray,
        iamToken: String,
        folderId: String,
    ): Result<String>
}
