package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.core.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.settings.navigation.SettingsRoute
import com.jacqulin.taskmanager.feature.settings.presentation.components.ColorPaletteRow
import com.jacqulin.taskmanager.feature.settings.presentation.components.SettingsSectionCard
import com.jacqulin.taskmanager.feature.settings.presentation.components.ThemeSchemeRow
import com.jacqulin.taskmanager.feature.settings.presentation.components.VoiceInputSettingsSection

@NavDestination(route = SettingsRoute::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.tokenSavedEvent.collect { name ->
            snackbar.showSnackbar("Данные для $name сохранены")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(titleRes = R.string.settings_title)
        }
    ) { paddingValues ->
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
                            selectedColorScheme = state.darkThemeConfig,
                            onColorSchemeSelected = viewModel::onDarkThemeConfigChanged
                        )
                    }
                )
            }

            item {
                SettingsSectionCard(
                    title = stringResource(R.string.settings_color_scheme),
                    content = {
                        ColorPaletteRow(
                            selectedPalette = state.colorPalette,
                            onPaletteSelected = viewModel::onColorPaletteChanged
                        )
                    }
                )
            }

            item {
                VoiceInputSettingsSection(
                    selectedProvider = state.selectedProvider,
                    fields = state.fields,
                    canSave = state.canSave,
                    isSaving = state.isSaving,
                    onProviderSelected = viewModel::onVoiceProviderChanged,
                    onCredentialChanged = viewModel::onCredentialChanged,
                    onSave = viewModel::onSaveVoiceSettingsClicked,
                )
            }

//            item {
//                Button(
//                    onClick = viewModel::resetSettings,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.errorContainer,
//                        contentColor = MaterialTheme.colorScheme.onErrorContainer
//                    ),
//                    border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.outline)
//                ) {
//                    Text(stringResource(R.string.settings_reset))
//                }
//            }
        }
    }
}


@NavPreview(route = SettingsRoute::class, primary = true)
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
