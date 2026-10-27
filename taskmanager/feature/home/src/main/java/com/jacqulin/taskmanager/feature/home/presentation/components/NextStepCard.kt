package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.home.presentation.HomeUiState
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.state.MainState

@Composable
fun NextStepCard(
    state: HomeUiState,
    onStartFocusClicked: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Следующий шаг".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .7f),
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = state.mainState.tasks.first().title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(4.dp))

            if (state.mainState.tasks.first().durationMinutes != null) {
                Text(
                    text = "${state.mainState.tasks.first().durationMinutes} мин на задачу",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .75f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Surface(
                onClick = onStartFocusClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (state.mainState.isFocusStarted) "Фокус начат"
                        else "Начать фокус",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun NextStepCardPreview() {
    TaskManagerTheme {
        NextStepCard(
            state = HomeUiState(
                mainState = MainState(
                    tasks = listOf(
                        HomeTaskUi(
                            id = 0,
                            title = "Task1",
                            isPlannedForToday = true,
                        ),
                        HomeTaskUi(
                            id = 1,
                            title = "Task2",
                            isPlannedForToday = true,
                        ),
                    )
                )
            ),
            onStartFocusClicked = {}
        )
    }
}

@Composable
@Preview
fun NextStepCardPreview_WithDuration() {
    TaskManagerTheme {
        NextStepCard(
            state = HomeUiState(
                mainState = MainState(
                    tasks = listOf(
                        HomeTaskUi(
                            id = 0,
                            title = "Task1",
                            isPlannedForToday = true,
                            durationMinutes = 10
                        ),
                        HomeTaskUi(
                            id = 1,
                            title = "Task2",
                            isPlannedForToday = true,
                        ),
                    )
                )
            ),
            onStartFocusClicked = {}
        )
    }
}

@Composable
@Preview
fun NextStepCardPreview_LongTitle() {
    TaskManagerTheme {
        NextStepCard(
            state = HomeUiState(
                mainState = MainState(
                    tasks = listOf(
                        HomeTaskUi(
                            id = 0,
                            title = "Task1fdsfdsfdfsdfdsfsdfsdfdsfgfdgdfgfgfdgfdgdfgfdgdfgdfgdfggdfgfdgfdgfg",
                            isPlannedForToday = true,
                            durationMinutes = 10
                        ),
                        HomeTaskUi(
                            id = 1,
                            title = "Task2",
                            isPlannedForToday = true,
                        ),
                    )
                )
            ),
            onStartFocusClicked = {}
        )
    }
}
