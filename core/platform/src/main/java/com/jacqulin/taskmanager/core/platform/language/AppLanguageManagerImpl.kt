package com.jacqulin.taskmanager.core.platform.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.jacqulin.taskmanager.core.model.AppLanguage
import javax.inject.Inject

internal class AppLanguageManagerImpl @Inject constructor() : AppLanguageManager {

    override fun getCurrentLanguage(): AppLanguage {
        val locales = AppCompatDelegate.getApplicationLocales()

        if (locales.isEmpty) {
            return AppLanguage.SYSTEM
        }

        return when (locales[0]?.language) {
            "ru" -> AppLanguage.RUSSIAN
            "en" -> AppLanguage.ENGLISH
            else -> AppLanguage.SYSTEM
        }
    }

    override fun setLanguage(language: AppLanguage) {
        val locales = when (language) {
            AppLanguage.SYSTEM ->
                LocaleListCompat.getEmptyLocaleList()

            AppLanguage.RUSSIAN ->
                LocaleListCompat.forLanguageTags("ru")

            AppLanguage.ENGLISH ->
                LocaleListCompat.forLanguageTags("en")
        }

        AppCompatDelegate.setApplicationLocales(locales)
    }
}
