package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeUiState
import com.jacqulin.taskmanager.feature.home.presentation.model.MainState

@Composable
fun FilledPlan(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onOpenFullPlan: () -> Unit,
    onAddTasksForPlanDay: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        PlanHeader(uiState = uiState)
        Spacer(Modifier.height(12.dp))
        PlanTaskList(
            uiState = uiState,
            onEvent = onEvent,
            onAddTasksForPlanDay = onAddTasksForPlanDay,
        )
        Spacer(Modifier.height(6.dp))
        PlanFooter(
            isTaskListEmpty = uiState.isEmpty,
            onOpenFullPlan = onOpenFullPlan,
        )
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

@Composable
@Preview(showSystemUi = true)
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
                ),
            ),
            onEvent = {},
            onAddTasksForPlanDay = {},
            onOpenFullPlan = {}
        )
    }
}
