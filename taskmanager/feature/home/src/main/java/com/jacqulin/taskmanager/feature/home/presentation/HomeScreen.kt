package com.jacqulin.taskmanager.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.home.presentation.components.EmptyPlanCard
import com.jacqulin.taskmanager.feature.home.presentation.components.FilledPlan
import com.jacqulin.taskmanager.feature.home.presentation.components.QuickCapture
import com.jacqulin.taskmanager.feature.home.presentation.components.AiFeatureCard
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.state.MainState

@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToFullPlan: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommitDayPlanOrderOnStop(viewModel::onEvent)

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToCreateTask -> onNavigateToTasks()
                HomeEffect.NavigateToCreateNote -> onNavigateToNotes()
            }
        }
    }

    HomeContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onOpenFullPlan = onNavigateToFullPlan,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onOpenFullPlan: () -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 12.dp)
    ) {
        item {
            Text(
                text = uiState.mainState.dateLabel,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (uiState.isEmpty) {
            item {
                EmptyPlanCard(
                    onAddTask = { onEvent(HomeEvent.OnAddTaskClicked) }
                )
            }
        } else {
            item {
                FilledPlan(
                    uiState = uiState,
                    onEvent = onEvent,
                    onOpenFullPlan = onOpenFullPlan,
                )
            }
        }

        item {
            QuickCapture(onEvent = onEvent)
        }

        item {
            AiFeatureCard(
                onClick = { onEvent(HomeEvent.OnBreakDownThoughtClicked) }
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun HomeContentPreview() {
    TaskManagerTheme(
        darkTheme = false
    ) {
        HomeContent(
            uiState = HomeUiState(
                mainState = MainState(
                    dateLabel = "вторник, 23 сентября",
                    tasks = listOf(
                        HomeTaskUi(
                            id = 1,
                            title = "написать реферат",
                            durationMinutes = 25,
                            isCompleted = false,
                            createdAtMillis = 0,
                            tags = emptyList(),
                            isPlannedForToday = true
                        )
                    )
                ),
            ),
            onEvent = { }
        ) { }
    }
}
