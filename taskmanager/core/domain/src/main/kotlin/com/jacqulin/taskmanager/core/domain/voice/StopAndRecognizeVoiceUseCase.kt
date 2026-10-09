package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.data.repository.AudioRecorderRepository
import com.jacqulin.taskmanager.data.repository.SpeechRecognitionRepository
import javax.inject.Inject

class StopAndRecognizeVoiceUseCase @Inject constructor(
    private val audioRecorderRepository: AudioRecorderRepository,
    private val speechRecognitionRepository: SpeechRecognitionRepository,
) {
    suspend operator fun invoke(): Result<String> {
        val audio = runCatching { audioRecorderRepository.stop() }
            .getOrElse {
                audioRecorderRepository.cancel()
                return Result.failure(it)
            }

        return speechRecognitionRepository.recognize(audio)
            .mapCatching { text ->
                if (text.isBlank()) error("Empty recognition result")
                text
            }
    }
}
