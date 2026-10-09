package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.data.repository.AudioRecorderRepository
import javax.inject.Inject

class CancelVoiceRecordingUseCase @Inject constructor(
    private val audioRecorderRepository: AudioRecorderRepository,
) {
    operator fun invoke() = audioRecorderRepository.cancel()
}
