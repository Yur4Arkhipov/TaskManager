package com.jacqulin.taskmanager.feature.notes.presentation.notebase.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun NotesToolbar(
    searchQuery: String,
    isDeleteModeEnabled: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onSortClick: () -> Unit,
    onDeleteModeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

        NotesToolbarButton(
            icon = painterResource(R.drawable.ic_sort),
            contentDescription = "",
            onClick = onSortClick
        )

        NotesToolbarButton(
            icon = painterResource(R.drawable.ic_delete),
            contentDescription = "",
            onClick = onDeleteModeClick,
            selected = isDeleteModeEnabled
        )
    }
}
