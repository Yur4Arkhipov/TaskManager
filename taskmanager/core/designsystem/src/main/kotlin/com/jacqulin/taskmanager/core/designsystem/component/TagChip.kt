package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.layout.padding
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
//    color: Color,
    isToday: Boolean = false,
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isToday) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainerHighest,
        contentColor = if (isToday) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Text(
            text = text,
//            style = MaterialTheme.typography.labelSmall,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
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
