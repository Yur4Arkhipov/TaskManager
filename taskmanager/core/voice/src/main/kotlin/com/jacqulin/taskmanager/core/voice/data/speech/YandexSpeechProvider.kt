package com.jacqulin.taskmanager.core.voice.data.speech

import android.util.Log
import com.jacqulin.taskmanager.core.voice.domain.SpeechException
import com.jacqulin.taskmanager.core.voice.domain.SpeechProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import javax.inject.Inject

class YandexSpeechProvider @Inject constructor(
    private val api: YandexSpeechApi,
) : SpeechProvider {

    override val id: String = "yandex-speech"

    override suspend fun recognize(
        credentials: Map<String, String>,
        audio: ByteArray,
    ): Result<String> = withContext(Dispatchers.IO) {
        val folderId = credentials["folderId"].orEmpty()
        val iamToken = credentials["iamToken"].orEmpty()

        if (folderId.isBlank() || iamToken.isBlank()) {
            return@withContext Result.failure(SpeechException.NotConfigured())
        }

        runCatching {
            val requestBody = audio.toRequestBody(
                contentType = "application/octet-stream".toMediaType(),
            )

            val response = try {
                api.recognize(
                    folderId = folderId,
                    authorization = "Bearer $iamToken",
                    audio = requestBody,
                )
            } catch (e: HttpException) {
                throw when (e.code()) {
                    401, 403 -> SpeechException.Unauthorized()
                    else -> e
                }
            }

            Log.d("speech", "send request")

            response.result.takeIf { it.isNotBlank() }
                ?: throw SpeechException.EmptyResult()
        }
    }
}