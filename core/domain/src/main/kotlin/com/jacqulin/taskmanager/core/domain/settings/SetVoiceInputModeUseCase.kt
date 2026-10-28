package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.data.repository.UserDataRepository
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import javax.inject.Inject

class SetVoiceInputModeUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(mode: VoiceInputMode) =
        userDataRepository.setVoiceInputMode(mode)
}
