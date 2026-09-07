package com.jacqulin.taskmanager.core.voice.data.speech

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface YandexSpeechApi {

    @POST("speech/v1/stt:recognize")
    suspend fun recognize(
        @Query("topic") topic: String = "general",
        @Query("folderId") folderId: String,
        @Query("lang") language: String = "ru-RU",
        @Query("format") format: String = "oggopus",
        @Header("Authorization") authorization: String,
        @Body audio: RequestBody,
    ): YandexSpeechResponse
}
