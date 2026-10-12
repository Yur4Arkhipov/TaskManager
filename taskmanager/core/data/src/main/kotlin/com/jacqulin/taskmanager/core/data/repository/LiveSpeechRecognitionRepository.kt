package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.model.voice.SpeechRecognitionEvent
import kotlinx.coroutines.flow.Flow

internal interface LiveSpeechRecognitionRepository {
    fun listen(): Flow<SpeechRecognitionEvent>
    fun stop()
}
