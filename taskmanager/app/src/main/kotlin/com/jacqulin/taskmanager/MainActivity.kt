package com.jacqulin.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.jacqulin.taskmanager.designsystem.theme.DarkColorSchemes
import com.jacqulin.taskmanager.designsystem.theme.LightColorSchemes
import com.jacqulin.taskmanager.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.settings.di.SettingsManager
import com.jacqulin.taskmanager.ui.App
import com.jacqulin.taskmanager.ui.rememberAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            val themePreference by settingsManager.getThemePreferenceString().collectAsState("SYSTEM")
            val colorSchemeLabel by settingsManager.colorScheme.collectAsState(
                com.jacqulin.taskmanager.feature.settings.presentation.ColorScheme.PURPLE
            )

            val isDarkTheme = when (themePreference) {
                "DARK" -> true
                "LIGHT" -> false
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            TaskManagerTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false,
                colorScheme = getResolvedColorScheme(isDarkTheme, colorSchemeLabel)
            ) {
                App(appState = appState)
            }
        }
    }

    @Composable
    private fun getResolvedColorScheme(
        isDark: Boolean,
        colorScheme: com.jacqulin.taskmanager.feature.settings.presentation.ColorScheme
    ): androidx.compose.material3.ColorScheme? {
        return if (isDark) {
            DarkColorSchemes[colorScheme.key]
        } else {
            LightColorSchemes[colorScheme.key]
        }
    }
}
