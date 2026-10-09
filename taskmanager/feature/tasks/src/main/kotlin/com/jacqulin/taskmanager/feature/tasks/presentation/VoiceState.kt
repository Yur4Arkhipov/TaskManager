package com.jacqulin.taskmanager.feature.tasks.presentation

sealed interface VoiceState {
    data object Idle : VoiceState
    data object Recording : VoiceState
    data object Processing : VoiceState
    data class Success(val text: String) : VoiceState
    data class Error(val error: VoiceError) : VoiceState
}
