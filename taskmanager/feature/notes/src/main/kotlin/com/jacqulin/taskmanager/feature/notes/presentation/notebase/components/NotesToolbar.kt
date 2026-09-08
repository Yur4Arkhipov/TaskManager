package com.jacqulin.taskmanager.feature.notes.presentation.notebase.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.SearchField
import com.jacqulin.taskmanager.designsystem.component.SortDropdownMenu
import com.jacqulin.taskmanager.designsystem.component.ToolbarButton
import com.jacqulin.taskmanager.designsystem.model.SortType

@Composable
fun NotesToolbar(
    searchQuery: String,
    isDeleteModeEnabled: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onSortChanged: (SortType) -> Unit,
    onDeleteModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        SearchField(
            value = searchQuery,
            placeholderText = "Поиск заметок...",
            onValueChange = onSearchQueryChanged,
            onSearch = onSearch,
            modifier = Modifier.weight(1f)
        )

        Box {
            ToolbarButton(
                icon = painterResource(R.drawable.ic_sort),
                contentDescription = "Сортировка",
                onClick = {
                    sortMenuExpanded = true
                }
            )

            SortDropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = {
                    sortMenuExpanded = false
                },
                onSortChanged = onSortChanged,
            )
        }

        ToolbarButton(
            icon = painterResource(R.drawable.ic_delete),
            contentDescription = "",
            onClick = onDeleteModeClick,
            selected = isDeleteModeEnabled
        )
    }
}
