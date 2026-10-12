package com.jacqulin.taskmanager.core.model.voice

/**
 * Represents the possible failure states reported by the speech recognition service.
 */
enum class SpeechRecognitionFailure {
    ServiceUnavailable,
    NoMatch,
    Network,
    AudioInput,
    PermissionDenied,
    ServiceBusy,
    TooManyRequests,
    UnsupportedLanguage,
    Unknown,
}
