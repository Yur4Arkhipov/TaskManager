package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeEvent
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeUiState
import sh.calvin.reorderable.ReorderableColumn

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
        if (!uiState.isEmpty) {
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        if (activeTasks.isNotEmpty()) {
            ReorderableColumn(
                list = activeTasks,
                onSettle = { fromIndex, toIndex ->
                    val visibleActiveIds = activeTasks.map { it.id }
                    if (fromIndex in visibleActiveIds.indices && toIndex in visibleActiveIds.indices) {
                        val reorderedVisibleActiveIds = visibleActiveIds.toMutableList().apply {
                            add(toIndex, removeAt(fromIndex))
                        }
                        val allTaskIds = uiState.mainState.tasks.map { it.id }
                        val visibleActiveIdSet = visibleActiveIds.toSet()
                        val activePositions = allTaskIds.indices.filter {
                            allTaskIds[it] in visibleActiveIdSet
                        }
                        val reorderedTaskIds = allTaskIds.toMutableList().apply {
                            activePositions.zip(reorderedVisibleActiveIds).forEach { (position, id) ->
                                this[position] = id
                            }
                        }
                        onEvent(HomeEvent.ReorderTasks(reorderedTaskIds))
                    }
                },
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) { _, task, _ ->
                key(task.id) {
                    ReorderableItem {
                        PlanTaskRow(
                            task = task,
                            onComplete = { onEvent(HomeEvent.UpdateTaskStatus(task)) },
                            onDelete = { onEvent(HomeEvent.OnTaskRemovedFromDayPlan(task)) },
                            modifier = Modifier.draggableHandle(),
                        )
                    }
                }
            }
        }

        completedTasks.forEach { task ->
            PlanTaskRow(
                task = task,
                onComplete = { onEvent(HomeEvent.UpdateTaskStatus(task)) },
                onDelete = { onEvent(HomeEvent.OnTaskRemovedFromDayPlan(task)) },
                isDragHandleEnabled = false,
            )
        }
    }
}
