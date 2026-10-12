package com.jacqulin.taskmanager.core.model.voice

/**
 * Platform-independent events emitted during a live speech recognition session.
 */
sealed interface SpeechRecognitionEvent {
    data object Ready : SpeechRecognitionEvent
    data object SpeechStarted : SpeechRecognitionEvent
    data object SpeechEnded : SpeechRecognitionEvent
    data class PartialResult(val text: String) : SpeechRecognitionEvent
    data class Result(val text: String) : SpeechRecognitionEvent
    data class Error(val reason: SpeechRecognitionFailure) : SpeechRecognitionEvent
}
