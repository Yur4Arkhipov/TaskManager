package com.jacqulin.taskmanager.core.data.repository

import android.os.Handler
import android.os.Looper
import android.speech.SpeechRecognizer
import android.util.Log
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionFailure
import com.jacqulin.taskmanager.core.speech.SpeechRecognitionListener
import com.jacqulin.taskmanager.core.speech.SystemSpeechRecognizer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LiveSpeechRecognitionRepositoryImpl @Inject constructor(
    private val systemSpeechRecognizer: SystemSpeechRecognizer,
) : LiveSpeechRecognitionRepository {

    // Keep SpeechRecognizer operations on the main thread
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun listen(): Flow<SpeechRecognitionEvent> = callbackFlow {
        if (!systemSpeechRecognizer.isAvailable) {
            trySend(SpeechRecognitionEvent.Error(SpeechRecognitionFailure.ServiceUnavailable))
            close()
            return@callbackFlow
        }

        mainHandler.post {
            try {
                systemSpeechRecognizer.startListening(
                    listener = object : SpeechRecognitionListener {
                        override fun onReadyForSpeech() {
                            trySend(SpeechRecognitionEvent.Ready)
                        }

                        override fun onBeginningOfSpeech() {
                            trySend(SpeechRecognitionEvent.SpeechStarted)
                        }

                        override fun onEndOfSpeech() {
                            trySend(SpeechRecognitionEvent.SpeechEnded)
                        }

                        override fun onPartialResults(results: List<String>) {
                            results.firstOrNull()?.let { text ->
                                trySend(SpeechRecognitionEvent.PartialResult(text))
                            }
                        }

                        override fun onResults(results: List<String>) {
                            val text = results.firstOrNull()
                            if (text.isNullOrBlank()) {
                                trySend(SpeechRecognitionEvent.Error(SpeechRecognitionFailure.NoMatch))
                            } else {
                                trySend(SpeechRecognitionEvent.Result(text))
                            }
                            close()
                        }

                        override fun onError(error: Int) {
                            trySend(SpeechRecognitionEvent.Error(error.toFailure()))
                            close()
                        }
                    },
                    partialResults = true
                )
            } catch (exception: Exception) {
                Log.e(TAG, "Failed to start speech recognition", exception)
                trySend(SpeechRecognitionEvent.Error(SpeechRecognitionFailure.ServiceUnavailable))
                close()
            }
        }

        awaitClose {
            mainHandler.post {
                systemSpeechRecognizer.destroy()
            }
        }
    }

    override fun stop() {
        mainHandler.post {
            systemSpeechRecognizer.stopListening()
        }
    }

    private fun Int.toFailure(): SpeechRecognitionFailure = when (this) {
        SpeechRecognizer.ERROR_NETWORK,
        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> SpeechRecognitionFailure.Network
        SpeechRecognizer.ERROR_NO_MATCH,
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> SpeechRecognitionFailure.NoMatch
        SpeechRecognizer.ERROR_AUDIO -> SpeechRecognitionFailure.AudioInput
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> SpeechRecognitionFailure.PermissionDenied
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> SpeechRecognitionFailure.ServiceBusy
        SpeechRecognizer.ERROR_TOO_MANY_REQUESTS -> SpeechRecognitionFailure.TooManyRequests
        SpeechRecognizer.ERROR_LANGUAGE_NOT_SUPPORTED,
        SpeechRecognizer.ERROR_LANGUAGE_UNAVAILABLE -> SpeechRecognitionFailure.UnsupportedLanguage
        SpeechRecognizer.ERROR_CLIENT,
        SpeechRecognizer.ERROR_SERVER,
        SpeechRecognizer.ERROR_SERVER_DISCONNECTED -> SpeechRecognitionFailure.ServiceUnavailable
        else -> SpeechRecognitionFailure.Unknown
    }

    private companion object {
        const val TAG = "LiveSpeechRepository"
    }
}
