package com.jacqulin.taskmanager.core.voice.di

import com.jacqulin.taskmanager.core.voice.BuildConfig
import com.jacqulin.taskmanager.core.voice.data.speech.YandexSpeechApi
import com.jacqulin.taskmanager.core.voice.data.speech.YandexSpeechConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YandexSpeechModule {

    @Provides
    @Singleton
    fun provideYandexSpeechConfig(): YandexSpeechConfig = YandexSpeechConfig(
        folderId = BuildConfig.YANDEX_FOLDER_ID,
        iamToken = BuildConfig.YANDEX_IAM_TOKEN,
    )

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            },
        )
        .build()

    @Provides
    @Singleton
    fun provideYandexSpeechApi(
        okHttpClient: OkHttpClient,
    ): YandexSpeechApi = Retrofit.Builder()
        .baseUrl("https://stt.api.cloud.yandex.net/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(YandexSpeechApi::class.java)
}
