package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeUiState

@Composable
fun PlanHeader(uiState: HomeUiState) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 12.dp
            ),
        ) {
            Text(
                text = stringResource(R.string.home_today_plan),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val taskCountText = if (uiState.totalMinutes > 0) {
                    stringResource(
                        R.string.home_task_count,
                        uiState.mainState.tasks.size,
                        taskCountWord(uiState.mainState.tasks.size),
                        uiState.totalMinutes,
                    )
                } else {
                    stringResource(
                        R.string.home_task_count_without_minutes,
                        uiState.mainState.tasks.size,
                        taskCountWord(uiState.mainState.tasks.size),
                    )
                }
                Text(
                    text = taskCountText,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(
                        R.string.home_progress_percentage,
                        (uiState.progress * 100).toInt()
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium)
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { uiState.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

private fun taskCountWord(count: Int): String = when {
    count % 10 == 1 && count % 100 != 11 -> "задача"
    count % 10 in 2..4 && count % 100 !in 12..14 -> "задачи"
    else -> "задач"
}
