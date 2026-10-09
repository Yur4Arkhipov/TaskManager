package com.jacqulin.taskmanager.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class YandexSpeechResponse(
    val result: String
)

