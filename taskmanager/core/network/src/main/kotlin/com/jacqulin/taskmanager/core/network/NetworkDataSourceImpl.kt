package com.jacqulin.taskmanager.core.network

import com.jacqulin.taskmanager.core.network.retrofit.YandexSpeechApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkDataSourceImpl @Inject constructor(
    private val api: YandexSpeechApi,
) : NetworkDataSource {

    override suspend fun recognize(
        audio: ByteArray,
        folderId: String,
        iamToken: String,
    ): Result<String> = withContext(Dispatchers.IO) {

        runCatching {
            val requestBody = audio.toRequestBody(
                contentType = "application/octet-stream".toMediaType(),
            )

            val response = api.recognize(
                folderId = folderId,
                authorization = "Bearer $iamToken",
                audio = requestBody,
            )

            response.result.takeIf { it.isNotBlank() }
                ?: error("Yandex returned an empty result")
        }
    }
}
