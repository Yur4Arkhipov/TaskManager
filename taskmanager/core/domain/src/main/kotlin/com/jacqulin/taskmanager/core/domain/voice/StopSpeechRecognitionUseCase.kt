package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.core.data.repository.LiveSpeechRecognitionRepository
import javax.inject.Inject

class StopSpeechRecognitionUseCase @Inject constructor(
    private val repository: LiveSpeechRecognitionRepository,
) {
    operator fun invoke() = repository.stop()
}
