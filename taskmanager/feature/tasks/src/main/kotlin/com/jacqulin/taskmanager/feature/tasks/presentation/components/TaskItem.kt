package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.core.designsystem.component.RoundedCheckbox
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun TaskItem(
    task: TaskItemUi,
    isDeleteModeEnabled: Boolean,
    onDeleteClick: () -> Unit,
    onCompleteClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    showCreationDate: Boolean = true,
    isCompact: Boolean = false,
    onSelectionClick: () -> Unit = {},
    onLongPress: () -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = { if (isDeleteModeEnabled) onSelectionClick() },
                onLongClick = onLongPress,
            ),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RoundedCheckbox(
                checked = if (isDeleteModeEnabled) isSelected else task.isCompleted,
                onCheckedChange = { checked ->
                    if (isDeleteModeEnabled) onSelectionClick() else onCompleteClick(checked)
                },
            )

            Spacer(modifier = Modifier.width(4.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.alpha(
                        if (task.isCompleted) 0.5f else 1f
                    ),
                    textDecoration = if (task.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        TextDecoration.None
                    }
                )
            }

            if (isDeleteModeEnabled) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable(
                            role = Role.Button,
                            onClick = onDeleteClick
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        if (!isDeleteModeEnabled && showCreationDate) {
            Box(
               contentAlignment = Alignment.TopEnd,
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = task.createdAtMillis.toFormattedDate(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                )
            }
        }
    }
}

private fun Long.toFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy'г.'")
        .withLocale(Locale.forLanguageTag("ru"))
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(formatter)
}

@Preview
@Composable
private fun TaskItemPreview() {
    TaskManagerTheme {
        TaskItem(
            task = TaskItemUi(
                id = 1,
                title = "Подготовить презентацию",
                createdAtMillis = System.currentTimeMillis(),
                isCompleted = false,
            ),
            isDeleteModeEnabled = false,
            onDeleteClick = {},
            onCompleteClick = {},
        )
    }
}

@Preview
@Composable
private fun TaskItemCompletedPreview() {
    TaskManagerTheme {
        TaskItem(
            task = TaskItemUi(
                id = 2,
                title = "Выполнить код-ревью",
                createdAtMillis = System.currentTimeMillis() - 86400000,
                isCompleted = true,
            ),
            isDeleteModeEnabled = true,
            onDeleteClick = {},
            onCompleteClick = {}
        )
    }
}
