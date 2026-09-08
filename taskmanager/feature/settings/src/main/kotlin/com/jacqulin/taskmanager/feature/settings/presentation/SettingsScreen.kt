package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.CenterAlignedTopAppBar
import com.jacqulin.taskmanager.feature.settings.navigation.SettingsRoute

@NavDestination(route = SettingsRoute::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CenterAlignedTopAppBar(
            titleRes = R.string.settings_title,
            navigationIcon = true,
            onNavigationClick = onNavigateBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Section 1: Token Balance (placeholder)
            SettingsSectionCard(
                title = "Баланс токенов",
                content = {
                    Text(
                        text = "0",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Theme Selection
            SettingsSectionCard(
                title = "Тема приложения",
                content = {
                    ThemeSelectionRow(
                        selectedTheme = uiState.themePreference,
                        onThemeSelected = { theme ->
                            viewModel.onEvent(SettingsEvent.OnThemeChanged(theme))
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Color Scheme
            SettingsSectionCard(
                title = "Цветовая схема",
                content = {
                    ColorSchemeRow(
                        selectedColorScheme = uiState.colorScheme,
                        onColorSchemeSelected = { colorScheme ->
                            viewModel.onEvent(SettingsEvent.OnColorSchemeChanged(colorScheme))
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Reset Settings Button
            Button(
                onClick = {
                    viewModel.onEvent(SettingsEvent.OnResetSettingsClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Сбросить настройки")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun ThemeSelectionRow(
    selectedTheme: ThemePreference,
    onThemeSelected: (ThemePreference) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        ThemePreference.entries.forEach { theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (theme == selectedTheme),
                        onClick = { onThemeSelected(theme) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (theme == selectedTheme),
                    onClick = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = theme.label,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun ColorSchemeRow(
    selectedColorScheme: ColorScheme,
    onColorSchemeSelected: (ColorScheme) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorScheme.entries.forEach { colorScheme ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .let { base ->
                                if (colorScheme == selectedColorScheme) {
                                    base.padding(2.dp)
                                } else {
                                    base
                                }
                            }
                            .background(
                                color = colorScheme.primary,
                                shape = CircleShape
                            ),
//                            .clickable(
//                                interactionSource = remember { MutableInteractionSource() },
//                                indication = null
//                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (colorScheme == selectedColorScheme) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = colorScheme.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@NavPreview(route = SettingsRoute::class, primary = true)
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
