package com.jacqulin.taskmanager.core.data.repository.language

import com.jacqulin.taskmanager.core.model.AppLanguage
import com.jacqulin.taskmanager.core.platform.language.AppLanguageManager
import javax.inject.Inject

internal class LanguageRepositoryImpl @Inject constructor(
    private val manager: AppLanguageManager
): LanguageRepository {

    override fun initialize() =
        manager.initialize()

    override fun getLanguage(): AppLanguage =
        manager.getLanguage()

    override fun setLanguage(language: AppLanguage) =
        manager.setLanguage(language)
}
