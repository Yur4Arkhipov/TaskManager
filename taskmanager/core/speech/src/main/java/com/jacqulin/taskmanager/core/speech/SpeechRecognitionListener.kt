package com.jacqulin.taskmanager.core.speech

/** Listener for results produced by Android's on-device/system speech recognition service. */
interface SpeechRecognitionListener {
    fun onReadyForSpeech() = Unit
    fun onBeginningOfSpeech() = Unit
    fun onEndOfSpeech() = Unit
    fun onResults(results: List<String>) = Unit
    fun onPartialResults(results: List<String>) = Unit
    fun onError(error: Int) = Unit
}
