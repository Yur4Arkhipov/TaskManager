package com.jacqulin.taskmanager.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jacqulin.taskmanager.core.designsystem.component.BottomNavigationBar
import com.jacqulin.taskmanager.core.designsystem.model.BottomBarItem
import com.jacqulin.taskmanager.core.voice.domain.VoiceRecognizer
import com.jacqulin.taskmanager.navigation.AppNavHost

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
internal fun App(
    appState: AppState,
    voiceRecognizer: VoiceRecognizer,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val bottomBarItems = appState.topLevelDestinations.map { destination ->
        BottomBarItem(
            icon = painterResource(destination.icon),
            contentDescription = stringResource(destination.titleTextId),
            selected = destination == appState.currentTopLevelDestination,
            enabled = !voiceRecognizer.isRecordingActive,
            onClick = {
                if (voiceRecognizer.isRecordingActive) {
                    Toast.makeText(context, "Завершите запись перед переходом", Toast.LENGTH_SHORT).show()
                } else {
                    appState.navigateToTopLevelDestination(destination)
                }
            }
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
