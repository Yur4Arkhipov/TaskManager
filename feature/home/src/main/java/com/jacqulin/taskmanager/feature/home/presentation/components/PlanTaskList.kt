package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.core.designsystem.extensions.fadeScrollableVerticalEdges
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeUiState
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun PlanTaskList(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onAddTasksForPlanDay: () -> Unit,
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
        if (uiState.mainState.availableTasksForPlan.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable(onClick = onAddTasksForPlanDay)
                    .padding(vertical = 2.dp, horizontal = 6.dp),
            ) {
                Text(
                    text = stringResource(R.string.home_add_tasks_to_plan),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))

    val visibleTasks = uiState.mainState.tasks.take(4)
    val activeTasks = visibleTasks.filterNot { it.isCompleted }
    val completedTasks = visibleTasks.filter { it.isCompleted }

    // Reorderable logic
    val taskIdsFromState = uiState.mainState.tasks.map { it.id } // Tasks ids from state
    var taskOrderIds by remember { mutableStateOf(emptyList<Int>()) } // User task order ids
    val reconciledTaskOrderIds = taskOrderIds.filter { it in taskIdsFromState } +
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

    LazyColumn(
        state = taskListState,
        modifier = Modifier.fadeScrollableVerticalEdges(taskListState, 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = orderedTasks,
            key = { it.id }
        ) { task ->
            val rowContent: @Composable (Modifier) -> Unit = { rowModifier ->
                PlanTaskRow(
                    task = task,
                    onComplete = { onEvent(HomeEvent.UpdateTaskCompletedStatus(task)) },
                    onDelete = { onEvent(HomeEvent.OnTaskRemovedFromDayPlan(task)) },
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
