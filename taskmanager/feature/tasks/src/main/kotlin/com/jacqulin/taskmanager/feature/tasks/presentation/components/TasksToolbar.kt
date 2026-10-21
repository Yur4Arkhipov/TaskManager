package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.component.SearchField
import com.jacqulin.taskmanager.core.designsystem.component.SortDropdownMenu
import com.jacqulin.taskmanager.core.designsystem.component.ToolbarButton
import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun TasksToolbar(
    searchQuery: String,
    isDeleteModeEnabled: Boolean,
    selectedCount: Int,
    allSelected: Boolean,
    sortType: SortType,
    showCreationDate: Boolean,
    showTags: Boolean,
    isCompactView: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onSortChanged: (SortType) -> Unit,
    onDeleteModeClick: () -> Unit,
    onSelectAllClick: () -> Unit,
    onShowCreationDateChanged: (Boolean) -> Unit,
    onShowTagsChanged: (Boolean) -> Unit,
    onCompactViewChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var settingsMenuExpanded by remember { mutableStateOf(false) }

    if (isDeleteModeEnabled) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.tasks_selected_count, selectedCount))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                TextButton(onClick = onSelectAllClick) {
                    Text(stringResource(if (allSelected) R.string.tasks_deselect_all else R.string.tasks_select_all))
                }
                ToolbarButton(
                    icon = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.tasks_exit_selection),
                    onClick = onDeleteModeClick,
                )
            }
        }
        return
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        SearchField(
            value = searchQuery,
            placeholderText = stringResource(R.string.tasks_search),
            onValueChange = onSearchQueryChanged,
            modifier = Modifier.weight(1f),
        )

        Box {
            ToolbarButton(
                icon = painterResource(R.drawable.ic_sort),
                contentDescription = stringResource(R.string.tasks_sort),
                onClick = { sortMenuExpanded = true },
            )

            SortDropdownMenu(
                expanded = sortMenuExpanded,
                sortType = sortType,
                onDismissRequest = { sortMenuExpanded = false },
                onSortChanged = onSortChanged,
            )
        }
        Box {
            ToolbarButton(
                icon = painterResource(R.drawable.ic_more_horiz),
                contentDescription = stringResource(R.string.tasks_list_settings),
                onClick = { settingsMenuExpanded = true },
            )
            DropdownMenu(
                expanded = settingsMenuExpanded,
                onDismissRequest = { settingsMenuExpanded = false },
                offset = DpOffset(x = 0.dp, y = 8.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 3.dp,
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.tasks_show_creation_date)) },
                    trailingIcon = {
                        CustomSwitch(
                            checked = showCreationDate,
                            onCheckedChange = onShowCreationDateChanged,
                        )
                    },
                    onClick = { onShowCreationDateChanged(!showCreationDate) },
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.tasks_show_tags)) },
                    trailingIcon = {
                        CustomSwitch(
                            checked = showTags,
                            onCheckedChange = onShowTagsChanged,
                        )
                    },
                    onClick = { onShowTagsChanged(!showTags) },
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.tasks_compact_view)) },
                    trailingIcon = {
                        CustomSwitch(
//                            checked = isCompactView,
//                            onCheckedChange = onCompactViewChanged,
                            checked = false,
                            onCheckedChange = { _ -> } ,
                        )
                    },
                    onClick = { /*onCompactViewChanged(!isCompactView)*/ },
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.tasks_select_tasks)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_select_window),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    onClick = {
                        settingsMenuExpanded = false
                        onDeleteModeClick()
                    },
                )
            }
        }
    }
}

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = Modifier.scale(0.6f)
    )
}

@Preview
@Composable
fun TasksToolbarPreview() {
    TasksToolbar(
        searchQuery = "",
        isDeleteModeEnabled = false,
        selectedCount = 0,
        allSelected = false,
        sortType = SortType.NEW_TO_OLD,
        showCreationDate = true,
        showTags = true,
        isCompactView = false,
        onSearchQueryChanged = {},
        onSortChanged = {},
        onDeleteModeClick = {},
        onSelectAllClick = {},
        onShowCreationDateChanged = {},
        onShowTagsChanged = {},
        onCompactViewChanged = {},
    )
}
