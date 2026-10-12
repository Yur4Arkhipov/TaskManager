package com.jacqulin.taskmanager.core.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.collections.orEmpty

/**
 * Thin wrapper around [SpeechRecognizer]. Calls must be made on the main thread.
 */
internal class SystemSpeechRecognizerImpl @Inject constructor(
    @ApplicationContext context: Context,
) : SystemSpeechRecognizer {
    private val appContext = context.applicationContext
    private var recognizer: SpeechRecognizer? = null
    private var listener: SpeechRecognitionListener? = null

    override val isAvailable: Boolean = SpeechRecognizer.isRecognitionAvailable(appContext)

    init {
        Log.i(TAG, "Speech recognition service available=$isAvailable")
    }

    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            Log.i(TAG, "Recognition service is ready")
            listener?.onReadyForSpeech()
        }

        override fun onBeginningOfSpeech() {
            Log.i(TAG, "Speech input detected")
            listener?.onBeginningOfSpeech()
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d(TAG, "Audio buffer callback; bytes=${buffer?.size ?: 0}")
        }

        override fun onEndOfSpeech() {
            Log.i(TAG, "Speech input ended")
            listener?.onEndOfSpeech()
        }

        override fun onResults(results: Bundle?) {
            val hypotheses = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).orEmpty()
            Log.i(TAG, "Final results received: ${hypotheses.size} hypotheses, lengths=${hypotheses.map(String::length)}")
            listener?.let {
                listener = null
                it.onResults(hypotheses)
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val hypotheses = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).orEmpty()
            Log.d(TAG, "Partial results received: ${hypotheses.size} hypotheses")
            listener?.onPartialResults(hypotheses)
        }

        override fun onError(error: Int) {
            Log.e(TAG, "Recognition failed: $error")
            listener?.let {
                listener = null
                it.onError(error)
            }
        }

        override fun onRmsChanged(rmsdB: Float) = Unit
        override fun onEvent(eventType: Int, params: Bundle?) = Unit
    }

    override fun startListening(
        listener: SpeechRecognitionListener,
        partialResults: Boolean,
    ) {
        check(isAvailable) { "No system speech recognition service is available" }
        this.listener = listener

        // startListening() must be called from the main thread of app
        getOrCreateRecognizer().startListening(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, partialResults)
            },
        )
    }

    override fun stopListening() {
        if (recognizer != null) {
            Log.i(TAG, "Stop requested")
            recognizer?.stopListening()
        }
    }

    override fun destroy() {
        if (recognizer != null) {
            Log.i(TAG, "Destroying recognizer")
            listener = null
            recognizer?.destroy()
            recognizer = null
        }
    }

    private fun getOrCreateRecognizer(): SpeechRecognizer = recognizer ?: SpeechRecognizer
        .createSpeechRecognizer(appContext)
        .also { it.setRecognitionListener(recognitionListener) }
        .also { recognizer = it }

    private companion object {
        private const val TAG = "SystemSpeechRecognizer"
    }
}
