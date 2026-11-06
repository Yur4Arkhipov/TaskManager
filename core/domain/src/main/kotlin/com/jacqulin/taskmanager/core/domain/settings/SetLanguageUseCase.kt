package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.data.repository.language.LanguageRepository
import com.jacqulin.taskmanager.core.model.AppLanguage
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: LanguageRepository
) {
    operator fun invoke(language: AppLanguage) = repository.setLanguage(language)
}
