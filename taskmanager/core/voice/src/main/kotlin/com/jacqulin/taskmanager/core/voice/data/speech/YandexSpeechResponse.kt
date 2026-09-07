package com.jacqulin.taskmanager.core.voice.data.speech

import kotlinx.serialization.Serializable

@Serializable
data class YandexSpeechResponse(
    val result: String
)
