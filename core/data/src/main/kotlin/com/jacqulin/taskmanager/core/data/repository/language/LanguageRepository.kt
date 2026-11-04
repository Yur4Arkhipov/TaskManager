package com.jacqulin.taskmanager.core.data.repository.language

import com.jacqulin.taskmanager.core.model.AppLanguage

interface LanguageRepository {
    fun getCurrentLanguage(): AppLanguage
    fun setLanguage(language: AppLanguage)
}
