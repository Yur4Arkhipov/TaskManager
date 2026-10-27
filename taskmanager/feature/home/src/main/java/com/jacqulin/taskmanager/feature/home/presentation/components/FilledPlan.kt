package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.HomeUiState
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.state.MainState

@Composable
fun FilledPlan(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onOpenFullPlan: () -> Unit = {},
) {
    Column {
        PlanHeader(uiState = uiState)

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
//                    Text(
//                        text = if (uiState.isFocusStarted) "Фокус начат"
//                            else "▶   Начать фокус",
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//            }
//        }

//        Spacer(Modifier.height(12.dp))

        PlanTaskList(
            uiState = uiState,
            onEvent = onEvent,
            onOpenFullPlan = onOpenFullPlan,
        )
    }
}

@Composable
private fun PlanTaskList(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onOpenFullPlan: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.home_day_plan),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        if (!uiState.isEmpty) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onOpenFullPlan)
                    .padding(vertical = 2.dp, horizontal = 6.dp),
            ) {
                Text(
                    text = stringResource(R.string.home_expand_plan),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
                Icon(
                    painter = painterResource(R.drawable.keyboard_arrow_right),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    val visibleTasks = uiState.mainState.tasks.take(4)

    visibleTasks.forEachIndexed { index, task ->
        PlanTaskRow(
            task = task,
            onComplete = { onEvent(HomeEvent.UpdateTaskStatus(task = task)) },
            onDelete = { onEvent(HomeEvent.OnTaskRemovedFromDayPlan(task)) },
        )
        Spacer(Modifier.height(6.dp))
    }
}

@Composable
private fun PlanHeader(uiState: HomeUiState) {
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
                Text(
                    stringResource(
                        R.string.home_task_count,
                        uiState.mainState.tasks.size,
                        taskCountWord(uiState.mainState.tasks.size),
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
                trackColor = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Preview
@Composable
private fun PlanHeaderPreview() {
    TaskManagerTheme {
        PlanHeader(
            uiState = HomeUiState(
                mainState = MainState(
                    tasks = listOf(
                        HomeTaskUi(id = 1, title = "Задача 1", durationMinutes = 45),
                        HomeTaskUi(id = 2, title = "Задача 2", durationMinutes = 30),
                        HomeTaskUi(id = 3, title = "Задача 3", durationMinutes = 20),
                    )
                )
            )
        )
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
            HomeTaskUi(id = 1, title = "Подготовить презентациюрпрпрпрпрпрпрпр", durationMinutes = 45),
            HomeTaskUi(id = 2, title = "Провести код-ревью", durationMinutes = 30),
            HomeTaskUi(id = 3, title = "Созвон с командой", durationMinutes = null),
            HomeTaskUi(id = 4, title = "Обновить документацию", durationMinutes = 20),
            HomeTaskUi(id = 5, title = "Созвон с командой", durationMinutes = 15),
        )

        FilledPlan(
            uiState = HomeUiState(
                mainState = MainState(
                    dateLabel = "Сегодня",
                    tasks = mockTasks,
//                    isFocusStarted = false,
//                    isFullPlanShown = false,
                ),
            ),
            onEvent = {}
        )
    }
}
