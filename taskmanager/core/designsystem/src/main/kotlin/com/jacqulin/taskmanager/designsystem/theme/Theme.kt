package com.jacqulin.taskmanager.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Color schemes for light and dark themes with custom color support.
 */
val DarkColorSchemes = mapOf<String, ColorScheme>(
    "PURPLE" to darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80
    ),
    "BLUE" to darkColorScheme(
        primary = Color(0xFF8AB4F8),
        secondary = Color(0xFFA8A8A8),
        tertiary = Color(0xFFA8D8A8)
    ),
    "GREEN" to darkColorScheme(
        primary = Color(0xFF8CD6B0),
        secondary = Color(0xFFA8A8A8),
        tertiary = Color(0xFFF0B088)
    ),
    "ORANGE" to darkColorScheme(
        primary = Color(0xFFE0B0F0),
        secondary = Color(0xFFB0A0F0),
        tertiary = Color(0xFFFFD080)
    ),
    "TEAL" to darkColorScheme(
        primary = Color(0xFF66D4DF),
        secondary = Color(0xFF8D99A8),
        tertiary = Color(0xFFB0A0F0)
    )
)

val LightColorSchemes = mapOf<String, ColorScheme>(
    "PURPLE" to lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40
    ),
    "BLUE" to lightColorScheme(
        primary = Color(0xFF3D5AFE),
        secondary = Color(0xFF5F6368),
        tertiary = Color(0xFF497D4C)
    ),
    "GREEN" to lightColorScheme(
        primary = Color(0xFF006D3B),
        secondary = Color(0xFF5F6368),
        tertiary = Color(0xFFB55D40)
    ),
    "ORANGE" to lightColorScheme(
        primary = Color(0xFFBA68C8),
        secondary = Color(0xFF7C4DFF),
        tertiary = Color(0xFFFFB74D)
    ),
    "TEAL" to lightColorScheme(
        primary = Color(0xFF007983),
        secondary = Color(0xFF4D576E),
        tertiary = Color(0xFF7C4DFF)
    )
)

@Composable
fun TaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {
    val resolvedColorScheme = colorScheme ?: run {
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorSchemes["PURPLE"]
            else -> LightColorSchemes["PURPLE"]
        } ?: lightColorScheme()
    }

    MaterialTheme(
        colorScheme = resolvedColorScheme,
        typography = Typography,
        content = content
    )
}
