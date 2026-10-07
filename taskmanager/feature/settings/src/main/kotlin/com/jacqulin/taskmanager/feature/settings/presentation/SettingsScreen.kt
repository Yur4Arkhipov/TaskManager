package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.DarkThemeConfig
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.designsystem.theme.TaskManagerTheme
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
                titleRes = titleRes
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
                            title = "Tема",
                            content = {
                                ThemeSchemeRow(
                                    selectedColorScheme = settingsUiState.settings.darkThemeConfig,
                                    onColorSchemeSelected = onChangeDarkThemeConfig
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
