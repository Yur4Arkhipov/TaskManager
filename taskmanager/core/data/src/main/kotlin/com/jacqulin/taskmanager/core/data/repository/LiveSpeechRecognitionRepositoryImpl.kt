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
        log("listen(): flow collected; thread=${Thread.currentThread().name}")
        if (!systemSpeechRecognizer.isAvailable) {
            log("listen(): recognizer unavailable")
            trySend(SpeechRecognitionEvent.Error(SpeechRecognitionFailure.ServiceUnavailable))
            close()
            return@callbackFlow
        }

        mainHandler.post {
            try {
                log("startListening(): running on ${Thread.currentThread().name}")
                systemSpeechRecognizer.startListening(
                    listener = object : SpeechRecognitionListener {
                        override fun onReadyForSpeech() {
                            log("callback onReadyForSpeech on ${Thread.currentThread().name}")
                            trySend(SpeechRecognitionEvent.Ready)
                        }

                        override fun onBeginningOfSpeech() {
                            log("callback onBeginningOfSpeech on ${Thread.currentThread().name}")
                            trySend(SpeechRecognitionEvent.SpeechStarted)
                        }

                        override fun onEndOfSpeech() {
                            log("callback onEndOfSpeech on ${Thread.currentThread().name}")
                            trySend(SpeechRecognitionEvent.SpeechEnded)
                        }

                        override fun onPartialResults(results: List<String>) {
                            log("callback onPartialResults on ${Thread.currentThread().name}")
                            results.firstOrNull()?.let { text ->
                                trySend(SpeechRecognitionEvent.PartialResult(text))
                            }
                        }

                        override fun onResults(results: List<String>) {
                            log("callback onResults on ${Thread.currentThread().name}")
                            val text = results.firstOrNull()
                            if (text.isNullOrBlank()) {
                                trySend(SpeechRecognitionEvent.Error(SpeechRecognitionFailure.NoMatch))
                            } else {
                                trySend(SpeechRecognitionEvent.Result(text))
                            }
                            close()
                        }

                        override fun onError(error: Int) {
                            log("callback onError code=$error on ${Thread.currentThread().name}")
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
            log("awaitClose(): flow collection ended; scheduling destroy")
            mainHandler.post {
                systemSpeechRecognizer.destroy()
            }
        }
    }

    override fun stop() {
        log("stop(): called on ${Thread.currentThread().name}; scheduling stopListening")
        mainHandler.post {
            log("stopListening(): running on ${Thread.currentThread().name}")
            systemSpeechRecognizer.stopListening()
        }
    }

    private fun log(message: String) {
        Log.d(TAG, "${System.currentTimeMillis()} $message")
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
