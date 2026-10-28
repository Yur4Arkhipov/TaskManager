package com.jacqulin.taskmanager.core.speech

/** Public contract for system speech recognition. */
interface SystemSpeechRecognizer {
    val isAvailable: Boolean
    fun startListening(
        listener: SpeechRecognitionListener,
        partialResults: Boolean,
    )
    fun stopListening()
    fun destroy()
}
