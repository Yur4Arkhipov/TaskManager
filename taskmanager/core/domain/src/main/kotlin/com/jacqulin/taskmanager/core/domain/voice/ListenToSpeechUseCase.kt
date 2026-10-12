package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.core.data.repository.LiveSpeechRecognitionRepository
import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenToSpeechUseCase @Inject constructor(
    private val repository: LiveSpeechRecognitionRepository,
) {
    operator fun invoke(): Flow<SpeechRecognitionEvent> = repository.listen()
}
