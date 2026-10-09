package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.settings.presentation.components.ColorPaletteRow
import com.jacqulin.taskmanager.feature.settings.presentation.components.SettingsSectionCard
import com.jacqulin.taskmanager.feature.settings.presentation.components.ThemeSchemeRow

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    SettingsScreen(
        titleRes = R.string.settings_title,
        settingsUiState = settingsUiState,
        onChangeDarkThemeConfig = viewModel::onDarkThemeConfigChanged,
        onChangeColorPalette = viewModel::onColorPaletteChanged
    )
}

@Composable
fun SettingsScreen(
    @StringRes titleRes: Int,
    settingsUiState: SettingsUiState,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    onChangeColorPalette: (colorPalette: ColorPalette) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                titleRes = R.string.settings_title
            )
        }
    ) { paddingValues ->
        when (settingsUiState) {
            SettingsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

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
                            title = stringResource(R.string.settings_tokens),
                            content = {
                                Text(
                                    text = "0",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        )
                    }

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

//                    item {
//                        Button(
//                            onClick = viewModel::resetSettings,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = MaterialTheme.colorScheme.errorContainer,
//                                contentColor = MaterialTheme.colorScheme.onErrorContainer
//                            ),
//                            border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.outline)
//                        ) {
//                            Text("Сбросить настройки")
//                        }
//                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    TaskManagerTheme() {
        SettingsScreen(
            titleRes = R.string.settings_title,
            settingsUiState = SettingsUiState.Success(
                settings = UserEditableSettings(
                    colorPalette = ColorPalette.DEFAULT,
                    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                ),
            ),
            onChangeDarkThemeConfig = {},
            onChangeColorPalette = {},
        )
    }
}
