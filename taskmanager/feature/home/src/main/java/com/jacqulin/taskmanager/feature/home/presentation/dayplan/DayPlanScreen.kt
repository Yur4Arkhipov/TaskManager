package com.jacqulin.taskmanager.feature.home.presentation.dayplan

import androidx.compose.animation.core.Spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.extensions.fadeScrollableVerticalEdges
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.CommitDayPlanOrderOnStop
import com.jacqulin.taskmanager.feature.home.presentation.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.HomeUiState
import com.jacqulin.taskmanager.feature.home.presentation.HomeViewModel
import com.jacqulin.taskmanager.feature.home.presentation.components.PlanTaskRow
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.state.MainState
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun DayPlanScreen(
    onBackClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CommitDayPlanOrderOnStop(viewModel::onEvent)

    DayPlanContent(
        state = state,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun DayPlanContent(
    state: HomeUiState,
    onBackClick: () -> Unit,
    onEvent: (HomeEvent) -> Unit,
) {
    val activeTasks = state.mainState.tasks.filterNot { it.isCompleted }
    val completedTasks = state.mainState.tasks.filter { it.isCompleted }
    val completedCount = state.mainState.tasks.size - activeTasks.size
    val progress = if (state.mainState.tasks.isEmpty()) 0f else completedCount.toFloat() / state.mainState.tasks.size
    val taskIdsFromState = state.mainState.tasks.map { it.id }
    var taskOrderIds by remember { mutableStateOf(emptyList<Int>()) }
    val knownTaskIds = taskIdsFromState.toSet()
    val reconciledTaskOrderIds = taskOrderIds.filter { it in knownTaskIds } +
        taskIdsFromState.filterNot { it in taskOrderIds }

    val activeTaskIdSet = activeTasks.mapTo(mutableSetOf()) { it.id }
    val activeTaskIds = reconciledTaskOrderIds.filter { it in activeTaskIdSet }
    val orderedActiveTasks = activeTaskIds.mapNotNull { id -> activeTasks.find { it.id == id } }
    val orderedTasks = orderedActiveTasks + completedTasks
    val taskListState = rememberLazyListState()
    val reorderableState = key(activeTaskIdSet) {
        rememberReorderableLazyListState(taskListState) { from, to ->
            val fromIndex = activeTaskIds.indexOf(from.key)
            val toIndex = activeTaskIds.indexOf(to.key)
            if (fromIndex in activeTaskIds.indices && toIndex in activeTaskIds.indices) {
                val reorderedActiveIds = activeTaskIds.toMutableList().apply {
                    add(toIndex, removeAt(fromIndex))
                }
                val activePositions = reconciledTaskOrderIds.indices.filter {
                    reconciledTaskOrderIds[it] in activeTaskIdSet
                }
                val updatedTaskOrderIds = reconciledTaskOrderIds.toMutableList().apply {
                    activePositions.zip(reorderedActiveIds).forEach { (position, id) ->
                        this[position] = id
                    }
                }
                taskOrderIds = updatedTaskOrderIds
                onEvent(HomeEvent.ReorderTasks(updatedTaskOrderIds))
            }
        }
    }

    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        BackRow(onBackClick)
        Spacer(Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = state.mainState.dateLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.home_day_plan_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.home_tasks_ahead, activeTasks.size),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(64.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            strokeWidth = 6.dp,
                        )
                        Text(
                            text = stringResource(
                                R.string.home_progress_percentage,
                                (progress * 100).toInt()
                            ),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        stringResource(R.string.home_in_progress),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(R.string.home_hold_and_drag),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(Modifier.height(10.dp))

            }

            item(key = "day_plan_tasks") {
                LazyColumn(
                    state = taskListState,
                    modifier = Modifier
                        .heightIn(max = 420.dp)
                        .fadeScrollableVerticalEdges(taskListState, 24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(orderedTasks, key = { it.id }) { task ->
                        val rowContent: @Composable (Modifier) -> Unit = { rowModifier ->
                            PlanTaskRow(
                                task = task,
                                onComplete = { onEvent(HomeEvent.UpdateTaskStatus(task)) },
                                onDelete = { onEvent(HomeEvent.OnTaskRemovedFromDayPlan(task)) },
                                isUpcoming = !task.isCompleted &&
                                    orderedActiveTasks.firstOrNull()?.id == task.id,
                                modifier = rowModifier,
                                isDragHandleEnabled = !task.isCompleted,
                            )
                        }
                        key(task.isCompleted) {
                            if (task.isCompleted) {
                                Box(
                                    modifier = Modifier.animateItem(
                                        placementSpec = spring(
                                            stiffness = Spring.StiffnessMediumLow,
                                        )
                                    )
                                ) {
                                    rowContent(Modifier)
                                }
                            } else {
                                ReorderableItem(
                                    state = reorderableState,
                                    key = task.id,
                                    animateItemModifier = Modifier.animateItem(
                                        placementSpec = spring(
                                            stiffness = Spring.StiffnessMediumLow,
                                        )
                                    ),
                                ) {
                                    rowContent(Modifier.draggableHandle())
                                }
                            }
                        }
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.home_completed_today).uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = stringResource(
                                R.string.home_completed_count,
                                completedCount,
                                state.mainState.tasks.size
                            ),
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = if (completedCount == 0) {
                                stringResource(R.string.home_completed_empty_hint)
                            } else {
                                stringResource(R.string.home_completed_hint)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BackRow(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onBackClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Назад",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun DayPlanContentPreview() {
    TaskManagerTheme {
        DayPlanContent(
            state = HomeUiState(
                mainState = MainState(
                    dateLabel = "вторник, 23 сентября",
                    tasks = listOf(
                        HomeTaskUi(
                            id = 1,
                            title = "Подготовить отчёт",
                            durationMinutes = 25,
                            isCompleted = false
                        ),
                        HomeTaskUi(
                            id = 2,
                            title = "Ответить на письма",
                            durationMinutes = 15,
                            isCompleted = false
                        ),
                    ),
                ),
            ),
            onBackClick = {},
            onEvent = {},
        )
    }
}
