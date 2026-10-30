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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
        BackRow(onBackClick)
        Spacer(Modifier.height(10.dp))

        Column {
            Text(
                text = "Теги",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Создайте тег один раз и используйте его во всем приложении",
                style = MaterialTheme.typography.bodyMedium,
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
                text = "Все метки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Log.d("TagsScreen", "Count tasks: ${state.visibleTags.size}")
            Text(
                text = if (state.visibleTags.isEmpty()) "Нет меток"
                else "${state.visibleTags.size} ${pluralize(
                    count = state.visibleTags.size,
                    one = "метка",
                    few = "метки",
                    many = "меток"
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
                placeholderText = "Поиск по меткам",
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

        val tagsListState = rememberLazyListState()
        LazyColumn(
            state = tagsListState,
            modifier = Modifier
                .fillMaxSize()
                .fadeScrollableVerticalEdges(tagsListState),
        ) {
            items(
                items = state.visibleTags,
                key = { it.tag.id }
            ) { item ->
                TagCard(
                    item = item,
                    onRename = { editing = item.tag },
                    onDelete = { deleting = item }
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }

    editing?.let { tag ->
        var name by remember(tag.id) { mutableStateOf(tag.name) }
        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Переименовать метку") },
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
                    Text("Сохранить")
                }
            },
            dismissButton = { TextButton(onClick = { editing = null }) { Text("Отмена") } },
        )
    }
    deleting?.let { item ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = {
                Text(
                    text = "Удалить метку «${item.tag.name}»?"
                )
            },
            text = {
                Text(
                    text = "Задачи и заметки сохранятся. Метка будет удалена из связанных задач."
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
                        text = "Удалить",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { deleting = null }
                ) {
                    Text("Отмена")
                }
            },
        )
    }
}

@Composable
private fun BackRow(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onBackClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Назад",  // Todo: изменить на "в настройки" или "в создание задачи",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
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
