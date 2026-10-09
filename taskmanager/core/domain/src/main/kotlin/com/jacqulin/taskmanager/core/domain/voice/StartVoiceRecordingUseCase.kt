package com.jacqulin.taskmanager.core.domain.voice

import com.jacqulin.taskmanager.data.repository.AudioRecorderRepository
import javax.inject.Inject

class StartVoiceRecordingUseCase @Inject constructor(
    private val audioRecorderRepository: AudioRecorderRepository,
) {
    operator fun invoke(): Result<Unit> = runCatching {
        audioRecorderRepository.start()
    }.onFailure {
        audioRecorderRepository.cancel()
    }
}
