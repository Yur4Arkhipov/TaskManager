package com.jacqulin.taskmanager.designsystem.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.jacqulin.taskmanager.designsystem.model.SortType

@Composable
fun SortDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSortChanged: (SortType) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            text = {
                Text("От новых к старым")
            },
            onClick = {
                onSortChanged(SortType.NEW_TO_OLD)
                onDismissRequest()
            },
        )

        DropdownMenuItem(
            text = {
                Text("От старых к новым")
            },
            onClick = {
                onSortChanged(SortType.OLD_TO_NEW)
                onDismissRequest()
            },
        )
    }
}
