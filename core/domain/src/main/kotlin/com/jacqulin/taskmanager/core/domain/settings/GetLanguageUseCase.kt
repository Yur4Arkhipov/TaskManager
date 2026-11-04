package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.data.repository.language.LanguageRepository
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    operator fun invoke() =
        repository.getCurrentLanguage()
}
