package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.network.NetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SpeechRecognitionRepositoryImpl @Inject constructor(
    private val network: NetworkDataSource,
) : SpeechRecognitionRepository {

    override suspend fun recognize(audio: ByteArray): Result<String> =
        network.recognize(audio, "", "")
}
