package com.jacqulin.taskmanager.feature.settings.presentation.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.component.BackRow
import com.jacqulin.taskmanager.core.designsystem.component.SearchField
import com.jacqulin.taskmanager.core.designsystem.component.TagsSortDropdownMenu
import com.jacqulin.taskmanager.core.designsystem.component.ToolbarButton
import com.jacqulin.taskmanager.core.designsystem.extensions.fadeScrollableVerticalEdges
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.settings.presentation.components.TagCard
import com.jacqulin.taskmanager.feature.settings.presentation.components.TagCreateCard
import com.jacqulin.taskmanager.feature.settings.presentation.model.TagManagementItem
import com.jacqulin.taskmanager.feature.settings.presentation.model.TagsEvent
import com.jacqulin.taskmanager.feature.settings.presentation.model.TagsUiState
import com.jacqulin.taskmanager.feature.settings.presentation.util.pluralize
import com.jacqulin.taskmanager.feature.settings.presentation.viewmodels.TagsViewModel

@Composable
fun TagsScreen(
    onBackClick: () -> Unit,
) {
    val viewModel = hiltViewModel<TagsViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TagsScreenContent(
        state = state,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun TagsScreenContent(
    state: TagsUiState,
    onBackClick: () -> Unit,
    onEvent: (TagsEvent) -> Unit
) {
//    var selectedColor by remember { mutableIntStateOf(0) }
    var editing by remember { mutableStateOf<Tag?>(null) }
    var deleting by remember { mutableStateOf<TagManagementItem?>(null) }

    var sortMenuExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val searchFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        BackRow(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onBackClick)
                .padding(4.dp)
        )
        Spacer(Modifier.height(10.dp))

        Column {
            Text(
                text = stringResource(R.string.settings_tags_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.settings_tags_screen_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                lineHeight = 14.sp
            )
        }
        Spacer(Modifier.height(12.dp))

        TagCreateCard(
            value = state.tagsState.createQuery,
//                        tagColors = tagColors,
//                    selectedColor = selectedColor,
//                    onSelectedColorChanged = {},
            onValueChanged = { new ->
                onEvent(TagsEvent.OnCreateQueryChanged(new))
            },
            onCreate = { title ->
                onEvent(TagsEvent.OnCreateTag(title))
            }
        )
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.settings_tags_all),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Log.d("TagsScreen", "Count tasks: ${state.visibleTags.size}")
            Text(
                text = if (state.visibleTags.isEmpty()) stringResource(R.string.settings_tags_empty)
                else "${state.visibleTags.size} ${pluralize(
                    count = state.visibleTags.size,
                    one = stringResource(R.string.tag_count_one),
                    few = stringResource(R.string.tag_count_few),
                    many = stringResource(R.string.tag_count_many)
                )}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.height(6.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            SearchField(
                value = state.tagsState.searchQuery,
                placeholderText = stringResource(R.string.settings_tags_search),
                onValueChange = { query ->
                    onEvent(TagsEvent.OnSearchQueryChanged(query))
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(searchFocusRequester),
                onSearch = { focusManager.clearFocus() }
            )
            Box {
                ToolbarButton(
                    icon = painterResource(R.drawable.ic_sort),
                    contentDescription = stringResource(R.string.tasks_sort),
                    onClick = { sortMenuExpanded = true },
                )

                TagsSortDropdownMenu(
                    expanded = sortMenuExpanded,
                    sortType = state.tagsState.sortType,
                    onDismissRequest = { sortMenuExpanded = false },
                    onSortChanged = { sortType ->
                        onEvent(TagsEvent.OnSortChanged(sortType))
                    },
                )
            }
        }
        Spacer(Modifier.height(12.dp))

        val gridState = rememberLazyGridState()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier
                .fillMaxSize()
                .fadeScrollableVerticalEdges(gridState),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.visibleTags, key = { it.tag.id }) { item ->
                TagCard(
                    item = item,
                    onRename = { editing = item.tag },
                    onDelete = { deleting = item }
                )
            }
        }
    }

    editing?.let { tag ->
        var name by remember(tag.id) { mutableStateOf(tag.name) }
        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text(stringResource(R.string.settings_tag_rename_title)) },
            text = {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (name.isNotBlank()) onEvent(TagsEvent.OnRenameTag(tag.id, name.trim()))
                        editing = null
                    }
                ) {
                    Text(stringResource(R.string.settings_tag_rename_confirm))
                }
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            dismissButton = { TextButton(onClick = { editing = null }) { Text(stringResource(R.string.settings_tag_rename_cancel)) } },
        )
    }
    deleting?.let { item ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = {
                Text(
                    text = stringResource(R.string.settings_tag_delete_title, item.tag.name)
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.settings_tag_delete_text),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(TagsEvent.OnDeleteTag(item.tag.id))
                        deleting = null
                    }
                ) {
                    Text(
                        text = stringResource(R.string.settings_tag_delete_button),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            dismissButton = {
                TextButton(
                    onClick = { deleting = null }
                ) {
                    Text(stringResource(R.string.settings_tag_rename_cancel))
                }
            },
        )
    }
}

@Composable
@Preview
fun TagsScreenContentPreview() {
    TaskManagerTheme {
        TagsScreenContent(
            state = TagsUiState(
                visibleTags = listOf(
                    TagManagementItem(
                        tag = Tag(0, "Работа"),
                        taskCount = 2
                    ),
                    TagManagementItem(
                        tag = Tag(1, "Учеба"),
                        taskCount = 4
                    ),
                    TagManagementItem(
                        tag = Tag(2, "Спорт"),
                        taskCount = 1
                    ),
                    TagManagementItem(
                        tag = Tag(3, "Книги"),
                        taskCount = 0
                    )
                ),
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}
