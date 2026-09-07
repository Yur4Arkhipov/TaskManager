package com.jacqulin.taskmanager.core.voice.data

import android.util.Log
import com.jacqulin.taskmanager.core.voice.domain.AudioRecorder
import com.jacqulin.taskmanager.core.voice.domain.SpeechToText
import com.jacqulin.taskmanager.core.voice.domain.VoiceError
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import com.jacqulin.taskmanager.core.voice.domain.VoiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class VoiceRecognizerImpl @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val speechToText: SpeechToText,
) : VoiceRecognizer {

    private val _state = MutableStateFlow<VoiceState>(
        VoiceState.Idle,
    )

    override val state: StateFlow<VoiceState> = _state.asStateFlow()

    override fun start() {
        if (_state.value is VoiceState.Recording) {
            return
        }

        try {
            audioRecorder.start()
            _state.value = VoiceState.Recording
            Log.d("Recognizer", "VoiceState: ${_state.value}")
        } catch (e: Exception) {
            audioRecorder.cancel()
            _state.value = VoiceState.Error(VoiceError.RecordingFailed)
        }
    }

    override suspend fun stop() {
        if (_state.value !is VoiceState.Recording) {
            return
        }

        _state.value = VoiceState.Processing

        val audio = try {
            audioRecorder.stop()
        } catch (e: Exception) {
            _state.value = VoiceState.Error(VoiceError.RecordingFailed)
            return
        }

        speechToText.recognize(audio)
            .onSuccess { text ->
                _state.value = VoiceState.Success(text)
            }
            .onFailure { throwable ->
                val error = when (throwable) {
                    is IllegalStateException -> VoiceError.Unknown
                    else -> VoiceError.RecognitionFailed
                }
                _state.value = VoiceState.Error(error)
            }
    }

    override fun cancel() {
        audioRecorder.cancel()
        _state.value = VoiceState.Idle
    }
}
