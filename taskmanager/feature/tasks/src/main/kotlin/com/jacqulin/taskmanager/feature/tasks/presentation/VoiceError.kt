package com.jacqulin.taskmanager.feature.tasks.presentation

sealed interface VoiceError {
    data object RecordingFailed : VoiceError
    data object RecognitionFailed : VoiceError
    data object Network : VoiceError
    data object Unauthorized : VoiceError
    data object EmptyResult : VoiceError
    data object Unknown : VoiceError
}
