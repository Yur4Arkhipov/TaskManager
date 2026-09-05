package com.jacqulin.taskmanager.feature.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.feature.settings.navigation.SettingsRoute

@NavDestination(route = SettingsRoute::class)
@Composable
fun SettingsScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Settings",
        )

    }
}


@NavPreview(route = SettingsRoute::class, primary = true)
@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
