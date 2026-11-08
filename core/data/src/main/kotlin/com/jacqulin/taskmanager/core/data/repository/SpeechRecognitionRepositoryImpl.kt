package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.network.NetworkDataSource
import com.jacqulin.taskmanager.core.platform.language.AppLanguageManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SpeechRecognitionRepositoryImpl @Inject constructor(
    private val network: NetworkDataSource,
    private val languageManager: AppLanguageManager,
) : SpeechRecognitionRepository {

    override suspend fun recognizeYandex(
        audio: ByteArray,
        iamToken: String,
        folderId: String,
    ): Result<String> = network.recognize(
        audio = audio,
        folderId = folderId,
        iamToken = iamToken,
        language = languageManager.getLanguage()
    )
}
