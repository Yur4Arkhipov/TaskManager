package com.jacqulin.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jacqulin.taskmanager.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.ui.App
import com.jacqulin.taskmanager.ui.rememberAppState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            TaskManagerTheme {
                App(appState = appState)
            }
        }
    }
}
