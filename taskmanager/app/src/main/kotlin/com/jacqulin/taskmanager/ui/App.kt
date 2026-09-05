package com.jacqulin.taskmanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jacqulin.taskmanager.designsystem.component.BottomNavigationBar
import com.jacqulin.taskmanager.designsystem.model.BottomBarItem
import com.jacqulin.taskmanager.navigation.AppNavHost

@Composable
internal fun App(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    val bottomBarItems = appState.topLevelDestinations.map { destination ->
        BottomBarItem(
            icon = painterResource(destination.icon),
            contentDescription = stringResource(destination.titleTextId),
            selected = destination == appState.currentTopLevelDestination,
            onClick = { appState.navigateToTopLevelDestination(destination) }
        )
    }

    Scaffold(
        modifier = modifier,
        bottomBar = { BottomNavigationBar(items = bottomBarItems) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavHost(appState = appState)
        }
    }
}
