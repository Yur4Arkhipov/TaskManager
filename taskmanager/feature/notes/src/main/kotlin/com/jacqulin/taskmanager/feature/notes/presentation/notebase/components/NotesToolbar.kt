package com.jacqulin.taskmanager.feature.notes.presentation.notebase.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
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
import com.jacqulin.taskmanager.feature.notes.presentation.notebase.NotesSortType

@Composable
fun NotesToolbar(
    searchQuery: String,
    isDeleteModeEnabled: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onSortChanged: (NotesSortType) -> Unit,
    onDeleteModeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        NotesSearchField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            onSearch = onSearch,
            modifier = Modifier.weight(1f),
        )

        Box {
            NotesToolbarButton(
                icon = painterResource(R.drawable.ic_sort),
                contentDescription = "Сортировка",
                onClick = {
                    sortMenuExpanded = true
                },
            )

            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = {
                    sortMenuExpanded = false
                },
            ) {
                DropdownMenuItem(
                    text = {
                        Text("От новых к старым")
                    },
                    onClick = {
                        onSortChanged(
                            NotesSortType.NEW_TO_OLD
                        )
                        sortMenuExpanded = false
                    },
                )

                DropdownMenuItem(
                    text = {
                        Text("От старых к новым")
                    },
                    onClick = {
                        onSortChanged(
                            NotesSortType.OLD_TO_NEW
                        )
                        sortMenuExpanded = false
                    },
                )
            }
        }

        NotesToolbarButton(
            icon = painterResource(R.drawable.ic_delete),
            contentDescription = "",
            onClick = onDeleteModeClick,
            selected = isDeleteModeEnabled
        )
    }
}
