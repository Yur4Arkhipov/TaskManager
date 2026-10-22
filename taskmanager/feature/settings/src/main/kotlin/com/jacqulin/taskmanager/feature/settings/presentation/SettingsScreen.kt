package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.settings.presentation.components.ColorPaletteRow
import com.jacqulin.taskmanager.feature.settings.presentation.components.ProviderConfigFields
import com.jacqulin.taskmanager.feature.settings.presentation.components.SettingsSectionCard
import com.jacqulin.taskmanager.feature.settings.presentation.components.ThemeSchemeRow
import com.jacqulin.taskmanager.feature.settings.presentation.components.TokenTextField
import com.jacqulin.taskmanager.feature.settings.presentation.components.VoiceProviderOptionCard
import com.jacqulin.taskmanager.feature.settings.presentation.model.SettingsEffect
import com.jacqulin.taskmanager.feature.settings.presentation.model.UserEditableSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateToTags: () -> Unit = {},
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        titleRes = R.string.settings_title,
        settingsUiState = settingsUiState,
        effects = viewModel.effects,
        onChangeDarkThemeConfig = { darkThemeConfig ->
            viewModel.onEvent(SettingsEvent.OnDarkThemeConfigChanged(darkThemeConfig))
        },
        onChangeColorPalette = { colorPalette ->
            viewModel.onEvent(SettingsEvent.OnColorPaletteChanged(colorPalette))
        },
        onChangeVoiceInputMode = { mode ->
            viewModel.onEvent(SettingsEvent.OnVoiceInputModeChanged(mode))
        },
        onYandexTokensChanged = { iamToken, folderId ->
            viewModel.onEvent(SettingsEvent.OnYandexTokensChanged(iamToken, folderId))
        },
        onNavigateToTags = onNavigateToTags,
    )
}

@Composable
fun SettingsScreen(
    @StringRes titleRes: Int,
    settingsUiState: SettingsUiState,
    effects: Flow<SettingsEffect>,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onChangeColorPalette: (colorPalette: ColorPalette) -> Unit,
    onChangeVoiceInputMode: (VoiceInputMode) -> Unit,
    onYandexTokensChanged: (String, String) -> Unit,
    onNavigateToTags: () -> Unit = {},
) {
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is SettingsEffect.ShowSuccess -> snackbar.showSnackbar(effect.message)
                is SettingsEffect.ShowError -> snackbar.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(titleRes = titleRes) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { paddingValues ->
        when (settingsUiState) {
            SettingsUiState.Loading -> { }
            is SettingsUiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        SettingsSectionCard(
                            title = stringResource(R.string.settings_theme),
                            content = {
                                ThemeSchemeRow(
                                    selectedThemeConfig = settingsUiState.settings.darkThemeConfig,
                                    onThemeConfigChanged = onChangeDarkThemeConfig
                                )
                            }
                        )
                    }

                    item {
                        SettingsSectionCard(
                            title = "Цветовая схема",
                            content = {
                                ColorPaletteRow(
                                    selectedPalette = settingsUiState.settings.colorPalette,
                                    onPaletteSelected = onChangeColorPalette
                                )
                            }
                        )
                    }

                    item {
                        SettingsSectionCard(
                            title = "Метки",
                            onClick = onNavigateToTags,
                            content = {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Управление метками задач",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Icon(
                                        painter = painterResource(R.drawable.keyboard_arrow_right),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        )
                    }

                    item {
                        var isVoiceInputExpanded by remember { mutableStateOf(false) }

                        SettingsSectionCard(
                            title = stringResource(R.string.settings_voice_input),
                            isExpanded = isVoiceInputExpanded,
                            onToggleExpanded = { isVoiceInputExpanded = !isVoiceInputExpanded },
                            content = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    val currentMode = settingsUiState.settings.voiceInputMode
                                    val isSystemVoiceInputSelected = currentMode == VoiceInputMode.LIVE
                                    VoiceInputMode.entries.forEachIndexed { index, mode ->
                                        val isSelected = currentMode == mode
                                        VoiceProviderOptionCard(
                                            title = getVoiceModeTitle(mode),
                                            subtitle = getVoiceModeSubtitle(mode),
                                            selected = isSelected,
                                            onClick = { onChangeVoiceInputMode(mode) }
                                        )

                                        if (isSelected) {
                                            ProviderConfigFields(
                                                voiceInputMode = mode,
                                                onYandexTokensChanged = onYandexTokensChanged,
                                                areYandexTokensConfigured = settingsUiState.areYandexTokensConfigured,
                                                onFutureTokenChanged = { }
                                            )
                                        }

                                        if (index < VoiceInputMode.entries.size - 1) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }

                                    if (!settingsUiState.speechRecognizerAvailable && isSystemVoiceInputSelected) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_info),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.width(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = stringResource(R.string.voice_recognizer_unavailable_warning),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getVoiceModeTitle(mode: VoiceInputMode): String =
    when (mode) {
        VoiceInputMode.LIVE -> stringResource(R.string.voice_mode_live)
        VoiceInputMode.YANDEX_SPEECH -> stringResource(R.string.voice_mode_yandex_speech)
        VoiceInputMode.FUTURE -> stringResource(R.string.coming_soon_label)
    }

@Composable
private fun getVoiceModeSubtitle(mode: VoiceInputMode): String =
    when (mode) {
        VoiceInputMode.LIVE -> stringResource(R.string.voice_mode_live_subtitle)
        VoiceInputMode.YANDEX_SPEECH -> stringResource(R.string.voice_mode_yandex_subtitle)
        VoiceInputMode.FUTURE -> stringResource(R.string.coming_soon_label)
    }

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    TaskManagerTheme {
        SettingsScreen(
            titleRes = R.string.settings_title,
            settingsUiState = SettingsUiState.Success(
                settings = UserEditableSettings(
                    colorPalette = ColorPalette.GREEN,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    voiceInputMode = VoiceInputMode.LIVE,
                ),
                speechRecognizerAvailable = true,
            ),
            effects = emptyFlow(),
            onChangeDarkThemeConfig = {},
            onChangeColorPalette = {},
            onChangeVoiceInputMode = {},
            onYandexTokensChanged = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreviewSpeechRecognizerUnavailable() {
    TaskManagerTheme {
        SettingsScreen(
            titleRes = R.string.settings_title,
            settingsUiState = SettingsUiState.Success(
                settings = UserEditableSettings(
                    colorPalette = ColorPalette.GREEN,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    voiceInputMode = VoiceInputMode.LIVE,
                ),
                speechRecognizerAvailable = false,
            ),
            effects = emptyFlow(),
            onChangeDarkThemeConfig = {},
            onChangeColorPalette = {},
            onChangeVoiceInputMode = {},
            onYandexTokensChanged = { _, _ -> },
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun TokenTextFieldPreview() {
    TaskManagerTheme {
        TokenTextField(
            value = "token",
            onValueChange = { },
            placeholder = R.string.yandex_iam_token_label,
        )
    }
}
