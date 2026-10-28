package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.data.repository.UserDataRepository
import javax.inject.Inject

class SetDarkThemeConfigUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(config: DarkThemeConfig) =
        userDataRepository.setDarkThemeConfig(config)
}
