package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter

@Composable
fun TasksFilter(
    taskFilter: TaskFilter,
    onTaskFilterChanged: (TaskFilter) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        TaskFilter.entries.forEach { filter ->
            val selected = taskFilter == filter
            val label = when (filter) {
                TaskFilter.ALL -> stringResource(R.string.tasks_filter_all)
                TaskFilter.ACTIVE -> stringResource(R.string.tasks_filter_active)
                TaskFilter.COMPLETED -> stringResource(R.string.tasks_filter_completed)
            }

            Surface(
                onClick = { onTaskFilterChanged(filter) },
                shape = CircleShape,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                },
                contentColor = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                )
            }
        }
    }
}

@Preview
@Composable
fun TasksFilterPreview() {
    TaskManagerTheme {
        TasksFilter(
            taskFilter = TaskFilter.ALL,
            onTaskFilterChanged = {}
        )
    }
}
