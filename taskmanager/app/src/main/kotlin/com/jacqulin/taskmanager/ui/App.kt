package com.jacqulin.taskmanager.ui

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.jacqulin.taskmanager.feature.home.navigation.DayPlanRoute
import com.jacqulin.taskmanager.core.designsystem.component.BottomNavigationBar
import com.jacqulin.taskmanager.core.designsystem.model.BottomBarItem
import com.jacqulin.taskmanager.feature.notes.navigation.NoteEditorRoute
import com.jacqulin.taskmanager.navigation.AppNavHost

@Composable
internal fun App(
    appState: AppState,
//    voiceRecognizer: VoiceRecognizer,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val bottomBarItems = appState.topLevelDestinations.map { destination ->
        BottomBarItem(
            icon = painterResource(destination.icon),
            contentDescription = stringResource(destination.titleTextId),
            selected = destination == appState.currentTopLevelDestination,
//            enabled = !voiceRecognizer.isRecordingActive,
            onClick = {
//                if (voiceRecognizer.isRecordingActive) {
//                    Toast.makeText(context, "Завершите запись перед переходом", Toast.LENGTH_SHORT).show()
//                } else {
                    appState.navigateToTopLevelDestination(destination)
//                }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            val hideBottomBar = appState.currentDestination.isOn<DayPlanRoute>() ||
                    appState.currentDestination.isOn<NoteEditorRoute>()

            if (!hideBottomBar) {
                BottomNavigationBar(items = bottomBarItems)
            }
        }
    ) { paddingValues ->
        AppNavHost(
            appState = appState,
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .imePadding()
        )
    }
}

inline fun <reified T : Any> NavDestination?.isOn(): Boolean =
    this?.hierarchy?.any { it.hasRoute<T>() } == true
