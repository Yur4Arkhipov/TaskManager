package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

sealed interface VoiceError {
    data object RecordingFailed : VoiceError
    data object RecognitionFailed : VoiceError
    data object Network : VoiceError
    data object Unauthorized : VoiceError
    data object EmptyResult : VoiceError
    data object Unknown : VoiceError
}
