package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.data.repository.UserDataRepository
import javax.inject.Inject

class AreYandexTokensConfiguredUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(): Boolean = userDataRepository.areYandexTokensConfigured()
}
