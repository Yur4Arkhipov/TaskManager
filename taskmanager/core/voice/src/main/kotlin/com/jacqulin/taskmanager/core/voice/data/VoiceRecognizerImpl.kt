package com.jacqulin.taskmanager.core.voice.data

import android.util.Log
import com.jacqulin.taskmanager.core.voice.domain.AudioRecorder
import com.jacqulin.taskmanager.core.voice.domain.SpeechToText
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import javax.inject.Inject

class VoiceRecognizerImpl @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val speechToText: SpeechToText
) : VoiceRecognizer {

    private var _isRecordingActive = false
    override val isRecordingActive: Boolean get() = _isRecordingActive

    override fun start() {
        _isRecordingActive = true
        try {
            audioRecorder.start()
            Log.d("Recognizer", "Recording started")
        } catch (e: Exception) {
            _isRecordingActive = false
            audioRecorder.cancel()
            throw e
        }
    }

    override suspend fun stopAndRecognize(): Result<String> {
        val audio = try {
            audioRecorder.stop()
        } catch (e: Exception) {
            _isRecordingActive = false
            audioRecorder.cancel()
            return Result.failure(IllegalStateException("Failed to stop recording"))
        }

        _isRecordingActive = false

        return speechToText.recognize(audio).mapCatching { text ->
            if (text.isBlank()) throw IllegalStateException("Empty recognition result")
            text
        }
    }

    override fun cancel() {
        _isRecordingActive = false
        audioRecorder.cancel()
    }
}
