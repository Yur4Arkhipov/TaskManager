package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.core.data.repository.AudioRecorderRepository
import javax.inject.Inject

class CancelVoiceRecordingUseCase @Inject constructor(
    private val audioRecorderRepository: AudioRecorderRepository,
) {
    suspend operator fun invoke() = audioRecorderRepository.cancel()
}
