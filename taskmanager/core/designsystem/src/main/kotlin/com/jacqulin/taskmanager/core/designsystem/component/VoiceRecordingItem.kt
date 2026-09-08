package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme

@Composable
fun VoiceRecordingItem(
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = 0.35f
        ),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_microphone),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.tasks_recording),
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(modifier = Modifier.height(4.dp))

                RecordingIndicator()
            }

            IconButton(
                onClick = onStopClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_cross),
                    contentDescription = stringResource(R.string.tasks_stop_recording)
                )
            }
        }
    }
}

@Preview
@Composable
fun VoiceRecordingItemPreview() {
    TaskManagerTheme {
        VoiceRecordingItem(
            onStopClick = { }
        )
    }
}