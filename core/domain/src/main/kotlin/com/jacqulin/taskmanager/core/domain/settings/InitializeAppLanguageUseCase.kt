package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.data.repository.language.LanguageRepository
import javax.inject.Inject

class InitializeAppLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    operator fun invoke() = repository.initialize()
}
