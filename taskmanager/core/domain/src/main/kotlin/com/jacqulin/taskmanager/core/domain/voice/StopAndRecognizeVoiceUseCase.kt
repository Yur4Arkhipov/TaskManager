package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.core.data.repository.AudioRecorderRepository
import com.jacqulin.taskmanager.core.data.repository.SpeechRecognitionRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class StopAndRecognizeVoiceUseCase @Inject constructor(
    private val audioRecorderRepository: AudioRecorderRepository,
    private val speechRecognitionRepository: SpeechRecognitionRepository,
) {
    suspend operator fun invoke(): Result<String> {
        val audio = try {
            audioRecorderRepository.stop()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return Result.failure(e)
        }

        return try {
            val text = speechRecognitionRepository.recognize(audio).getOrThrow()
            if (text.isNotBlank()) {
                Result.success(text)
            } else {
                Result.failure(IllegalStateException("Empty recognition result"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
