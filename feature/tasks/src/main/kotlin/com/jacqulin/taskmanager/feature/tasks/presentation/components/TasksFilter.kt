package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter

@Composable
fun TasksFilter(
    isDeleteMode: Boolean,
    allSelected: Boolean,
    onSelectAllClicked: () -> Unit,
    taskFilter: TaskFilter,
    onTaskFilterChanged: (TaskFilter) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
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
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                contentColor = if (selected) {
                    MaterialTheme.colorScheme.onSecondaryContainer
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

        if (isDeleteMode) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(
                    if (allSelected) R.string.tasks_deselect_all else R.string.tasks_select_all
                ),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = onSelectAllClicked
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun TasksFilterPreview() {
    TaskManagerTheme {
        TasksFilter(
            allSelected = true,
            isDeleteMode = true,
            taskFilter = TaskFilter.ALL,
            onTaskFilterChanged = {},
            onSelectAllClicked = {}
        )
    }
}
