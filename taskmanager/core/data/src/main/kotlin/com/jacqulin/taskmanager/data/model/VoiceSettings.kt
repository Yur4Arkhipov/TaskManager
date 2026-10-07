package com.jacqulin.taskmanager.data.model

data class VoiceSettings(
    val providerId: String,
    val credentials: Map<String, String>,
)
