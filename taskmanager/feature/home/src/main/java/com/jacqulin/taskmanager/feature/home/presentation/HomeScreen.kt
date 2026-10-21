package com.jacqulin.taskmanager.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.home.presentation.components.EmptyPlanCard
import com.jacqulin.taskmanager.feature.home.presentation.components.FilledPlan
import com.jacqulin.taskmanager.feature.home.presentation.components.QuickCapture
import com.jacqulin.taskmanager.feature.home.presentation.components.AiFeatureCard

@Composable
fun HomeScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToNotes: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

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
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
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
                text = uiState.dateLabel,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (uiState.tasks.isEmpty()) {
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
                dateLabel = "вторник, 23 сентября"
            )
        ) { }
    }
}
