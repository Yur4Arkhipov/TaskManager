package com.jacqulin.taskmanager.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jacqulin.taskmanager.core.designsystem.component.BottomNavigationBar
import com.jacqulin.taskmanager.core.designsystem.model.BottomBarItem
import com.jacqulin.taskmanager.navigation.AppNavHost

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
        bottomBar = {
            BottomNavigationBar(
                items = bottomBarItems
            )
        }
    ) { paddingValues  ->
        AppNavHost(
            appState = appState,
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding()
            )
        )
    }
}
