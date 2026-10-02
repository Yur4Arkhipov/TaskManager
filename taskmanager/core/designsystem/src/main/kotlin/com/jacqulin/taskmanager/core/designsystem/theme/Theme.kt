package com.jacqulin.taskmanager.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jacqulin.taskmanager.core.model.ColorPalette

val DarkCyanColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Purple20,
    primaryContainer = Purple30,
    onPrimaryContainer = Purple90,
    secondary = Orange80,
    onSecondary = Orange20,
    secondaryContainer = Orange30,
    onSecondaryContainer = Orange90,
    tertiary = Blue80,
    onTertiary = Blue20,
    tertiaryContainer = Blue30,
    onTertiaryContainer = Blue90,
    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,
    background = DarkPurpleGray10,
    onBackground = DarkPurpleGray90,
    surface = DarkPurpleGray10,
    onSurface = DarkPurpleGray90,
    surfaceVariant = PurpleGray30,
    onSurfaceVariant = PurpleGray80,
    inverseSurface = DarkPurpleGray90,
    inverseOnSurface = DarkPurpleGray10,
    outline = PurpleGray60,
)

val LightCyanColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = Purple90,
    onPrimaryContainer = Purple10,
    secondary = Orange40,
    onSecondary = Color.White,
    secondaryContainer = Orange90,
    onSecondaryContainer = Orange10,
    tertiary = Blue40,
    onTertiary = Color.White,
    tertiaryContainer = Blue90,
    onTertiaryContainer = Blue10,
    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,
    background = DarkPurpleGray99,
    onBackground = DarkPurpleGray10,
    surface = DarkPurpleGray99,
    onSurface = DarkPurpleGray10,
    surfaceVariant = PurpleGray90,
    onSurfaceVariant = PurpleGray30,
    inverseSurface = DarkPurpleGray20,
    inverseOnSurface = DarkPurpleGray95,
    outline = PurpleGray50,
)

val LightBlueColorScheme = lightColorScheme(
    primary = Color(0xFF005AC1),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD8E2FF),
    onPrimaryContainer = Color(0xFF001A41),
    secondary = Color(0xFF575E71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDBE2F9),
    onSecondaryContainer = Color(0xFF141B2C),
    background = Color(0xFFFEFBFF),
    onBackground = Color(0xFF1B1B1F),
    surface = Color(0xFFFEFBFF),
    onSurface = Color(0xFF1B1B1F),
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44474F),
    outline = Color(0xFF757780)
)

val DarkBlueColorScheme = darkColorScheme(
    primary = Color(0xFFAEC6FF),
    onPrimary = Color(0xFF002E69),
    primaryContainer = Color(0xFF004494),
    onPrimaryContainer = Color(0xFFD8E2FF),
    secondary = Color(0xFFBFC6DC),
    onSecondary = Color(0xFF293041),
    secondaryContainer = Color(0xFF3F4759),
    onSecondaryContainer = Color(0xFFDBE2F9),
    background = Color(0xFF1B1B1F),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF1B1B1F),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF44474F),
    onSurfaceVariant = Color(0xFFC5C6D0),
    outline = Color(0xFF8F9099)
)

val LightGreenColorScheme = lightColorScheme(
    primary = Color(0xFF006D3A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF99F6B5),
    onPrimaryContainer = Color(0xFF00210D),
    secondary = Color(0xFF516354),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD3E8D5),
    onSecondaryContainer = Color(0xFF0E1F13),
    tertiary = Color(0xFF396570),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFBCEBF7),
    onTertiaryContainer = Color(0xFF001F26),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFBFDF8),
    onBackground = Color(0xFF191C19),
    surface = Color(0xFFFBFDF8),
    onSurface = Color(0xFF191C19),
    surfaceVariant = Color(0xFFDDE5DB),
    onSurfaceVariant = Color(0xFF414941),
    outline = Color(0xFF717971)
)

val DarkGreenColorScheme = darkColorScheme(
    primary = Color(0xFF7CDA99),
    onPrimary = Color(0xFF00391C),
    primaryContainer = Color(0xFF00522A),
    onPrimaryContainer = Color(0xFF99F6B5),
    secondary = Color(0xFFB8CCB9),
    onSecondary = Color(0xFF233427),
    secondaryContainer = Color(0xFF394B3D),
    onSecondaryContainer = Color(0xFFD3E8D5),
    tertiary = Color(0xFFA1CEDA),
    onTertiary = Color(0xFF00363F),
    tertiaryContainer = Color(0xFF1F4D57),
    onTertiaryContainer = Color(0xFFBCEBF7),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF191C19),
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF191C19),
    onSurface = Color(0xFFE1E3DF),
    surfaceVariant = Color(0xFF414941),
    onSurfaceVariant = Color(0xFFC1C9BF),
    outline = Color(0xFF8B938A)
)

val LightPurpleColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFEF7FF),
    onBackground = Color(0xFF1D1B20),
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E)
)

val DarkPurpleColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99)
)

val LightOrangeColorScheme = lightColorScheme(
    primary = Color(0xFF8B4F25),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDBC9),
    onPrimaryContainer = Color(0xFF311100),
    secondary = Color(0xFF775741),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDBC9),
    onSecondaryContainer = Color(0xFF2C1605),
    tertiary = Color(0xFF5D6033),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE2E5AC),
    onTertiaryContainer = Color(0xFF1A1C00),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF5DED3),
    onSurfaceVariant = Color(0xFF53433C),
    outline = Color(0xFF85736B)
)

val DarkOrangeColorScheme = darkColorScheme(
    primary = Color(0xFFFFB59B),
    onPrimary = Color(0xFF4F2500),
    primaryContainer = Color(0xFF6E370A),
    onPrimaryContainer = Color(0xFFFFDBC9),
    secondary = Color(0xFFE7BDA3),
    onSecondary = Color(0xFF442A17),
    secondaryContainer = Color(0xFF5D402B),
    onSecondaryContainer = Color(0xFFFFDBC9),
    tertiary = Color(0xFFC6C992),
    onTertiary = Color(0xFF2F3208),
    tertiaryContainer = Color(0xFF45491D),
    onTertiaryContainer = Color(0xFFE2E5AC),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF201A17),
    onBackground = Color(0xFFEDE0DB),
    surface = Color(0xFF201A17),
    onSurface = Color(0xFFEDE0DB),
    surfaceVariant = Color(0xFF53433C),
    onSurfaceVariant = Color(0xFFD8C2B8),
    outline = Color(0xFFA08D84)
)

val LightAmberColorScheme = lightColorScheme(
    primary = Color(0xFF845926),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDECA),
    onPrimaryContainer = Color(0xFF2D1900),
    secondary = Color(0xFF715D39),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFDE0B0),
    onSecondaryContainer = Color(0xFF271900),
    tertiary = Color(0xFF536742),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD7EDBC),
    onTertiaryContainer = Color(0xFF112105),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF5DFC8),
    onSurfaceVariant = Color(0xFF534638),
    outline = Color(0xFF857564),
    inverseSurface = Color(0xFF352F2C),
    inverseOnSurface = Color(0xFFFBF0EA),
)

val DarkAmberColorScheme = darkColorScheme(
    primary = Color(0xFFFFB984),
    onPrimary = Color(0xFF482E00),
    primaryContainer = Color(0xFF634114),
    onPrimaryContainer = Color(0xFFFFDECA),
    secondary = Color(0xFFE0C38F),
    onSecondary = Color(0xFF402F0D),
    secondaryContainer = Color(0xFF594522),
    onSecondaryContainer = Color(0xFFFDE0B0),
    tertiary = Color(0xFFBBD191),
    onTertiary = Color(0xFF253615),
    tertiaryContainer = Color(0xFF3C4D2B),
    onTertiaryContainer = Color(0xFFD7EDBC),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF201A17),
    onBackground = Color(0xFFEDE0DA),
    surface = Color(0xFF201A17),
    onSurface = Color(0xFFEDE0DA),
    surfaceVariant = Color(0xFF534638),
    onSurfaceVariant = Color(0xFFD8C5B1),
    outline = Color(0xFFA08F7D),
    inverseSurface = Color(0xFFE6D9D2),
    inverseOnSurface = Color(0xFF352F2C),
)

val LightTealColorScheme = lightColorScheme(
    primary = Color(0xFF006A6A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFA2F4F4),
    onPrimaryContainer = Color(0xFF002020),
    secondary = Color(0xFF4D6363),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCFE9E8),
    onSecondaryContainer = Color(0xFF091F1F),
    tertiary = Color(0xFF46637F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFD0E6FF),
    onTertiaryContainer = Color(0xFF001E33),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFAFDFD),
    onBackground = Color(0xFF191C1C),
    surface = Color(0xFFFAFDFD),
    onSurface = Color(0xFF191C1C),
    surfaceVariant = Color(0xFFDCE4E4),
    onSurfaceVariant = Color(0xFF404949),
    outline = Color(0xFF707979),
    inverseSurface = Color(0xFF2E3131),
    inverseOnSurface = Color(0xFFF1F4F4),
)

val DarkTealColorScheme = darkColorScheme(
    primary = Color(0xFF56DADA),
    onPrimary = Color(0xFF003B3B),
    primaryContainer = Color(0xFF005151),
    onPrimaryContainer = Color(0xFFA2F4F4),
    secondary = Color(0xFFB3CCCB),
    onSecondary = Color(0xFF1F3535),
    secondaryContainer = Color(0xFF364B4B),
    onSecondaryContainer = Color(0xFFCFE9E8),
    tertiary = Color(0xFFB0CAE9),
    onTertiary = Color(0xFF14334B),
    tertiaryContainer = Color(0xFF2E4A64),
    onTertiaryContainer = Color(0xFFD0E6FF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF191C1C),
    onBackground = Color(0xFFE0E3E3),
    surface = Color(0xFF191C1C),
    onSurface = Color(0xFFE0E3E3),
    surfaceVariant = Color(0xFF404949),
    onSurfaceVariant = Color(0xFFBFC8C8),
    outline = Color(0xFF899393),
    inverseSurface = Color(0xFFE0E3E3),
    inverseOnSurface = Color(0xFF2E3131),
)

@Composable
fun TaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorPalette: ColorPalette = ColorPalette.CYAN,
    content: @Composable () -> Unit
) {
    val colorScheme = when (colorPalette) {
        ColorPalette.CYAN -> if (darkTheme) DarkCyanColorScheme else LightCyanColorScheme
        ColorPalette.BLUE -> if (darkTheme) DarkBlueColorScheme else LightBlueColorScheme
        ColorPalette.GREEN -> if (darkTheme) DarkGreenColorScheme else LightGreenColorScheme
        ColorPalette.PURPLE -> if (darkTheme) DarkPurpleColorScheme else LightPurpleColorScheme
        ColorPalette.ORANGE -> if (darkTheme) DarkOrangeColorScheme else LightOrangeColorScheme
        ColorPalette.AMBER -> if (darkTheme) DarkAmberColorScheme else LightAmberColorScheme
        ColorPalette.TEAL -> if (darkTheme) DarkTealColorScheme else LightTealColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
