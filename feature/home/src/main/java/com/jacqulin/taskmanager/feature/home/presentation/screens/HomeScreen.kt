package com.jacqulin.taskmanager.feature.home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.jacqulin.taskmanager.core.designsystem.component.CreateTaskBottomSheet
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.home.presentation.components.AddTasksForPlanBottomSheet
import com.jacqulin.taskmanager.feature.home.presentation.components.EmptyPlanCard
import com.jacqulin.taskmanager.feature.home.presentation.components.FilledPlan
import com.jacqulin.taskmanager.feature.home.presentation.components.QuickCapture
import com.jacqulin.taskmanager.feature.home.presentation.lifecycle.CommitDayPlanOrderOnStop
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEffect
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeUiState
import com.jacqulin.taskmanager.feature.home.presentation.model.MainState
import com.jacqulin.taskmanager.feature.home.presentation.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    onNavigateToNotes: () -> Unit,
    onNavigateToFullPlan: () -> Unit,
    onNavigateToTags: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommitDayPlanOrderOnStop {
        viewModel.onEvent(HomeEvent.CommitDayPlanOrder)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToTags -> onNavigateToTags()
                HomeEffect.NavigateToCreateNote -> onNavigateToNotes()
                HomeEffect.NavigateToFullPlan -> onNavigateToFullPlan()
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
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .padding(top = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = uiState.mainState.dateLabel,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (uiState.isEmpty) {
            EmptyPlanCard(
                onAddTask = { onEvent(HomeEvent.OnAddTasksForPlanClicked) }
            )
        } else {
            FilledPlan(
                uiState = uiState,
                onEvent = onEvent,
                onAddTasksForPlanDay = { onEvent(HomeEvent.OnAddTasksForPlanClicked) },
                onOpenFullPlan = { onEvent(HomeEvent.OnOpenFullPlanClicked) }
            )
        }

        QuickCapture(onEvent = onEvent)
    }

    if (uiState.mainState.isCreateTaskBottomSheetVisible) {
        CreateTaskBottomSheet(
            title = uiState.taskState.title,
            availableTags = uiState.mainState.availableTags,
            selectedTags = uiState.taskState.tags,
            isPlannedForToday = uiState.taskState.isPlannedForToday,
            onDismissRequest = { onEvent(HomeEvent.OnBottomSheetDismissed) },
            onTitleChanged = { title -> onEvent(HomeEvent.OnTaskTextChanged(title)) },
            onPlanForTodayChanged = { enabled -> onEvent(HomeEvent.OnTaskPlanForTodayChanged(enabled)) },
            onTagAdded = { tag -> onEvent(HomeEvent.OnTaskTagAdded(tag)) },
            onTagRemoved = { tag -> onEvent(HomeEvent.OnTaskTagRemoved(tag)) },
            onSaveTaskClick = { onEvent(HomeEvent.OnSaveTaskClicked) },
            onNavigateToTags = { onEvent(HomeEvent.OnCreateTagClicked) }
        )
    }

    if (uiState.mainState.isAddTaskForPlanBottomSheetVisible) {
        AddTasksForPlanBottomSheet(
            state = uiState.mainState,
            onTaskSelectionChange = { taskId -> onEvent(HomeEvent.ToggleTaskSelectionForPlan(taskId)) },
            onDismissRequest = { onEvent(HomeEvent.OnBottomSheetDismissed) },
            onSaveClick = { tasksIds -> onEvent(HomeEvent.OnAddTasksForPlanSaved(tasksIds)) },
        )
    }
}

@Composable
@Preview
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
            onEvent = { },
        )
    }
}
