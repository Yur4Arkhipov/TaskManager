package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.core.designsystem.component.RoundedCheckbox
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.HomeUiState
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi

@Composable
fun FilledPlan(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
) {
    Column {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
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
                    Text(
                        stringResource(
                            R.string.home_task_count,
                            uiState.tasks.size,
                            taskCountWord(uiState.tasks.size),
                            uiState.totalMinutes
                        ),
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
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                )
            }
        }

        Spacer(Modifier.height(12.dp))

//        Surface(
//            shape = RoundedCornerShape(24.dp),
//            color = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.onPrimary,
//        ) {
//            Column(Modifier.padding(18.dp)) {
//                Text("СЛЕДУЮЩИЙ ШАГ", style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .7f), fontWeight = FontWeight.Bold)
//                Spacer(Modifier.height(12.dp))
//                Text(uiState.tasks.first().title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
//                Spacer(Modifier.height(6.dp))
//                Text("◷   ${uiState.tasks.first().durationMinutes} мин на всю задачу",
//                    style = MaterialTheme.typography.labelMedium,
//                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .75f))
//                Spacer(Modifier.height(16.dp))
//                Button(
//                    onClick = { onEvent(HomeEvent.OnStartFocusClicked) },
//                    modifier = Modifier.fillMaxWidth().height(44.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.surface,
//                        contentColor = MaterialTheme.colorScheme.primary,
//                    ),
//                ) {
//                    Text(if (uiState.isFocusStarted) "Фокус начат" else "▶   Начать фокус", fontWeight = FontWeight.SemiBold)
//                }
//            }
//        }

//        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val interactionSource = remember { MutableInteractionSource() }

            Text(
                text = stringResource(R.string.home_day_plan),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.tasks_add),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onEvent(HomeEvent.OnAddToPlanClicked) },
                    )
            )
        }

        Spacer(Modifier.height(12.dp))

        val visibleTasks = if (uiState.isFullPlanShown) uiState.tasks
            else uiState.tasks.take(3)

        visibleTasks.forEachIndexed { index, task ->
            PlanTaskRow(
                task = task,
                position = index + 1,
                onComplete = {  },
                onDelete = { onEvent(HomeEvent.OnTaskDeleted(task.id)) },
            )
            Spacer(Modifier.height(6.dp))
        }
        if (uiState.tasks.size > 3) {
            Text(
                if (uiState.isFullPlanShown)
                    stringResource(R.string.home_collapse_plan)
                else
                    stringResource(R.string.home_expand_plan),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .clickable { onEvent(HomeEvent.OnShowFullPlanClicked) }
                    .padding(vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun PlanTaskRow(
    task: HomeTaskUi,
    position: Int,
    onComplete: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_drag_indicator),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

//            Spacer(Modifier.width(10.dp))

//            Text(
//                text = position.toString(),
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                style = MaterialTheme.typography.labelMedium
//            )

            Spacer(Modifier.width(10.dp))

            RoundedCheckbox(
                checked = task.isCompleted,
                onCheckedChange = { checked -> onComplete(checked) },
                cornerRadius = 6.dp,
            )

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (task.hasDuration) {
                    Text(
                        text = "${task.durationMinutes} мин",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Text("×", color = MaterialTheme.colorScheme.outline, fontSize = 22.sp,
                modifier = Modifier.clickable(onClick = onDelete).padding(start = 8.dp))
        }
    }
}

private fun taskCountWord(count: Int): String = when {
    count % 10 == 1 && count % 100 != 11 -> "задача"
    count % 10 in 2..4 && count % 100 !in 12..14 -> "задачи"
    else -> "задач"
}

@Composable
@Preview(
    showSystemUi = true
)
fun PlanFilledPreview() {
    TaskManagerTheme {
        val mockTasks = listOf(
            HomeTaskUi(id = 1, title = "Подготовить презентацию", durationMinutes = 45),
            HomeTaskUi(id = 2, title = "Провести код-ревью", durationMinutes = 30),
            HomeTaskUi(id = 3, title = "Созвон с командой", durationMinutes = null),
            HomeTaskUi(id = 4, title = "Обновить документацию", durationMinutes = 20),
            HomeTaskUi(id = 5, title = "Созвон с командой", durationMinutes = 15),
        )

        FilledPlan(
            uiState = HomeUiState(
                dateLabel = "Сегодня",
                tasks = mockTasks,
                isFocusStarted = false,
                isFullPlanShown = false,
            ),
            onEvent = {}
        )
    }
}
