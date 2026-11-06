package com.jacqulin.taskmanager.core.platform.language

import com.jacqulin.taskmanager.core.model.AppLanguage

interface AppLanguageManager {
    fun initialize()
    fun getLanguage(): AppLanguage
    fun setLanguage(language: AppLanguage)
}
