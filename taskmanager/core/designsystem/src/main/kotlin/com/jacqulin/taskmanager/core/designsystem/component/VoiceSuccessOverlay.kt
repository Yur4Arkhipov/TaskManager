package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun VoiceSuccessOverlay(
    modifier: Modifier = Modifier,
    taskTitle: String
) {
//    val scale = remember { Animatable(0f) }
//
//    LaunchedEffect(Unit) {
//        scale.animateTo(
//            targetValue = 1f,
//            animationSpec = spring(
//                dampingRatio = 0.6f,
//                stiffness = 300f
//            )
//        )
//    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .blur(radius = 10.dp)
            .background(Color.Black.copy(alpha = 0.6f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = stringResource(R.string.tasks_successful),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = taskTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.tasks_voice_successfully_return),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun VoiceSuccessOverlayPreview() {
    TaskManagerTheme {
        VoiceSuccessOverlay(
            taskTitle = ""
        )
    }
}
