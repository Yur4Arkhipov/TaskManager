package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun BottomBar(
    onAddToPlan: () -> Unit,
    onRemoveFromPlan: () -> Unit,
    onDelete: () -> Unit,
    addToPlanEnabled: Boolean,
    removeFromPlanEnabled: Boolean,
    deleteEnabled: Boolean,
    addToPlanCount: Int,
    removeFromPlanCount: Int,
    deleteCount: Int,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        BarTextButton(
            text = stringResource(R.string.tasks_add_to_plan, addToPlanCount),
            onClick = onAddToPlan,
            enabled = addToPlanEnabled,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )

        BarTextButton(
            text = stringResource(R.string.tasks_remove_from_plan, removeFromPlanCount),
            onClick = onRemoveFromPlan,
            enabled = removeFromPlanEnabled,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )

        BarTextButton(
            text = stringResource(R.string.tasks_delete_selected, deleteCount),
            onClick = onDelete,
            enabled = deleteEnabled,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}

@Composable
private fun BarTextButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    containerColor: Color,
    contentColor: Color,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    TaskManagerTheme {
        BottomBar(
            onDelete = {},
            addToPlanEnabled = false,
            deleteEnabled = false,
            addToPlanCount = 1,
            onAddToPlan = {},
            onRemoveFromPlan = {},
            removeFromPlanEnabled = false,
            removeFromPlanCount = 1,
            deleteCount = 1,
        )
    }
}
