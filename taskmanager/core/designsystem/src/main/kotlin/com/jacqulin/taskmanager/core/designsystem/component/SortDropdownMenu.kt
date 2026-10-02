package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.model.SortType

@Composable
fun SortDropdownMenu(
    expanded: Boolean,
    sortType: SortType,
    onDismissRequest: () -> Unit,
    onSortChanged: (SortType) -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(x = 0.dp, y = 8.dp),
        shape = shape,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 3.dp,
    ) {
        SortMenuItem(
            text = "От новых к старым",
            selected = sortType == SortType.NEW_TO_OLD,
            onClick = {
                onSortChanged(SortType.NEW_TO_OLD)
                onDismissRequest()
            }
        )

        SortMenuItem(
            text = "От старых к новым",
            selected = sortType == SortType.OLD_TO_NEW,
            onClick = {
                onSortChanged(SortType.OLD_TO_NEW)
                onDismissRequest()
            }
        )
    }
}

@Composable
private fun SortMenuItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val background = if (selected)
        MaterialTheme.colorScheme.secondaryContainer
    else
        Color.Transparent

    val contentColor = if (selected)
        MaterialTheme.colorScheme.onSecondaryContainer
    else
        MaterialTheme.colorScheme.onSurface

    DropdownMenuItem(
        text = { Text(text = text, color = contentColor) },
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background),
    )
}
