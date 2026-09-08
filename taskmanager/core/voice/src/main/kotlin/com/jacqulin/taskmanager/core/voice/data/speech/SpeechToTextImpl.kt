package com.jacqulin.taskmanager.core.voice.data.speech

import android.util.Log
import com.jacqulin.taskmanager.core.voice.domain.SpeechToText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SpeechToTextImpl @Inject constructor(
    private val api: YandexSpeechApi,
    private val config: YandexSpeechConfig,
) : SpeechToText {

    override suspend fun recognize(
        audio: ByteArray
    ): Result<String> = withContext(Dispatchers.IO) {
        val folderId = config.folderId
        val iamToken = config.iamToken

        if (folderId.isBlank() || iamToken.isBlank()) {
            return@withContext Result.failure(IllegalStateException("Yandex speech config is not configured"))
        }

        runCatching {
            val requestBody = audio.toRequestBody(
                contentType = "application/octet-stream".toMediaType(),
            )

            val response = api.recognize(
                folderId = folderId,
                authorization = "Bearer $iamToken",
                audio = requestBody,
            )
            Log.d("speech", "send request")

            response.result
                .takeIf { it.isNotBlank() }
                ?: error("Yandex returned an empty result")
        }
    }
}
