package com.jacqulin.taskmanager.core.platform.language

import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.jacqulin.taskmanager.core.model.AppLanguage
import javax.inject.Inject

internal class AppLanguageManagerImpl @Inject constructor() : AppLanguageManager {

    override fun initialize() {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (!currentLocales.isEmpty) return
        val systemLocale = Resources.getSystem()
            .configuration.locales[0]

        val initialLanguage = if (systemLocale.language == "ru")
            AppLanguage.RUSSIAN
        else
            AppLanguage.ENGLISH


        setLanguage(initialLanguage)
    }

    override fun getLanguage(): AppLanguage {
        val currentLocale = AppCompatDelegate
            .getApplicationLocales()
            .get(0)

        return when (currentLocale?.language) {
            "ru" -> AppLanguage.RUSSIAN
            "en" -> AppLanguage.ENGLISH
            else -> AppLanguage.ENGLISH
        }
    }

    override fun setLanguage(language: AppLanguage) {
        val languageTag = when (language) {
            AppLanguage.RUSSIAN -> "ru"
            AppLanguage.ENGLISH -> "en"
        }

        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageTag)
        )
    }
}
