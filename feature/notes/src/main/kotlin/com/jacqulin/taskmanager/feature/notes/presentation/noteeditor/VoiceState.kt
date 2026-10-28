package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

sealed interface VoiceState {
    data object Idle : VoiceState
    data class Recording(val partialText: String = "") : VoiceState
    data object Processing : VoiceState
    data class Success(val text: String) : VoiceState
    data class Error(val error: VoiceError) : VoiceState
}
