package com.jacqulin.taskmanager.feature.home.presentation.focus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme

@Composable
fun FocusConfigScreen(

) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
       Text("Focus config screen")
    }
}


@Composable
@Preview
fun FocusConfigScreenPreview() {
    TaskManagerTheme {
        FocusConfigScreen()
    }
}
