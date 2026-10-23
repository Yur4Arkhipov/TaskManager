package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme

@Composable
fun TagChip(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    isToday: Boolean = false,
    isSelected: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isToday) MaterialTheme.colorScheme.primaryContainer
            else if (isSelected) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = if (isToday) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier,
        )
    }
}

@Composable
@Preview
fun TaskTagChipPreview() {
    TaskManagerTheme {
        TagChip(
            text = "Work",
            textStyle = MaterialTheme.typography.labelSmall
        )
    }
}
