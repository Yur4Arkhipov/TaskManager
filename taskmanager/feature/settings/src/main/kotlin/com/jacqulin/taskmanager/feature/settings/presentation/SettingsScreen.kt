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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.jacqulin.taskmanager.feature.settings.presentation.components.SettingsSectionCard
import com.jacqulin.taskmanager.feature.settings.presentation.components.ThemeSchemeRow
import com.jacqulin.taskmanager.feature.settings.presentation.components.VoiceProviderOptionCard
import com.jacqulin.taskmanager.feature.settings.presentation.model.UserEditableSettings

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        titleRes = R.string.settings_title,
        settingsUiState = settingsUiState,
        onChangeDarkThemeConfig = viewModel::onDarkThemeConfigChanged,
        onChangeColorPalette = viewModel::onColorPaletteChanged,
        onChangeVoiceInputMode = viewModel::onVoiceInputModeChanged,
    )
}

@Composable
fun SettingsScreen(
    @StringRes titleRes: Int,
    settingsUiState: SettingsUiState,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onChangeColorPalette: (colorPalette: ColorPalette) -> Unit,
    onChangeVoiceInputMode: (VoiceInputMode) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(titleRes = titleRes) }
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
                        var isVoiceInputExpanded by remember { mutableStateOf(true) }

                        SettingsSectionCard(
                            title = stringResource(R.string.settings_voice_input),
                            isExpanded = isVoiceInputExpanded,
                            onToggleExpanded = { isVoiceInputExpanded = !isVoiceInputExpanded },
                            content = {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    VoiceInputMode.entries.forEachIndexed { index, mode ->
                                        val isSelected = settingsUiState.settings.voiceInputMode == mode
                                        VoiceProviderOptionCard(
                                            title = getVoiceModeTitle(mode),
                                            subtitle = getVoiceModeSubtitle(mode),
                                            selected = isSelected,
                                            onClick = { onChangeVoiceInputMode(mode) }
                                        )
                                        if (index < VoiceInputMode.entries.size - 1) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }

                                    if (!settingsUiState.speechRecognizerAvailable) {
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
    }

@Composable
private fun getVoiceModeSubtitle(mode: VoiceInputMode): String =
    when (mode) {
        VoiceInputMode.LIVE -> stringResource(R.string.voice_mode_live_subtitle)
        VoiceInputMode.YANDEX_SPEECH -> stringResource(R.string.voice_mode_yandex_subtitle)
    }

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    TaskManagerTheme {
        SettingsScreen(
            titleRes = R.string.settings_title,
            settingsUiState = SettingsUiState.Success(
                settings = UserEditableSettings(
                    colorPalette = ColorPalette.CYAN,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    voiceInputMode = VoiceInputMode.LIVE,
                ),
                speechRecognizerAvailable = true,
            ),
            onChangeDarkThemeConfig = {},
            onChangeColorPalette = {},
            onChangeVoiceInputMode = {},
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
                    colorPalette = ColorPalette.CYAN,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    voiceInputMode = VoiceInputMode.LIVE,
                ),
                speechRecognizerAvailable = false,
            ),
            onChangeDarkThemeConfig = {},
            onChangeColorPalette = {},
            onChangeVoiceInputMode = {},
        )
    }
}
