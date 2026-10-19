package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.network.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SpeechRecognitionRepositoryImpl @Inject constructor(
    private val network: NetworkDataSource,
) : SpeechRecognitionRepository {

    override suspend fun recognizeYandex(
        audio: ByteArray,
        iamToken: String,
        folderId: String,
    ): Result<String> = network.recognize(
        audio = audio,
        folderId = folderId,
        iamToken = iamToken,
    )
}
