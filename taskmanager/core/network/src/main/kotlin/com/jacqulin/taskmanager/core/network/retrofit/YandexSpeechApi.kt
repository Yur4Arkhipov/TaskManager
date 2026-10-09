package com.jacqulin.taskmanager.core.network.retrofit

import com.jacqulin.taskmanager.core.network.dto.YandexSpeechResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

internal interface YandexSpeechApi {

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
