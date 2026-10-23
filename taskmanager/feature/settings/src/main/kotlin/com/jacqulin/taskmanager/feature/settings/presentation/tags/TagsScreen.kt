package com.jacqulin.taskmanager.feature.settings.presentation.tags

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.component.SearchField
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.settings.presentation.model.TagManagementItem

@Composable
fun TagsScreen(
    onBackClick: () -> Unit,
    viewModel: TagsViewModel = hiltViewModel(),
) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    TagsScreenContent(
        items = items,
        onBackClick = onBackClick,
        onCreate = viewModel::create,
        onRename = viewModel::rename,
        onDelete = viewModel::delete,
    )
}

@Composable
private fun TagsScreenContent(
    items: List<TagManagementItem>,
    onBackClick: () -> Unit,
    onCreate: (String) -> Unit,
    onRename: (Int, String) -> Unit,
    onDelete: (Int) -> Unit,
) {
    var newName by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var selectedColor by remember { mutableIntStateOf(0) }
    var sortByUsage by remember { mutableStateOf(true) }
    var editing by remember { mutableStateOf<Tag?>(null) }
    var deleting by remember { mutableStateOf<TagManagementItem?>(null) }
    val filtered = items.filter { it.tag.name.contains(query.trim(), ignoreCase = true) }
        .let { if (sortByUsage) it.sortedByDescending(TagManagementItem::taskCount) else it.sortedBy { item -> item.tag.name.lowercase() } }

    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        BackRow(onBackClick)

        Spacer(Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            item {
                Column {
                    Text(
                        text = "Теги",
                        style = MaterialTheme.typography.displaySmall,
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
            }

            item {
                TagCreateCard(
                    value = newName,
//                        tagColors = tagColors,
                    selectedColor = selectedColor,
                    onSelectedColorChanged = { newColor ->
                        selectedColor = newColor
                    },
                    onValueChanged = { new ->
                        newName = new
                    },
                    onCreate = onCreate
                )
                Spacer(Modifier.height(12.dp))
            }

            item {
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
                    Text(
                        "${items.size} меток",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(6.dp))
                SearchField(
                    value = query,
                    placeholderText = "Поиск по меткам",
                    onValueChange = { query = it },
                )
                Spacer(Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row {
                        SortPill(
                            text = "По названию",
                            selected = !sortByUsage,
                            onClick = { sortByUsage = false },
                            modifier = Modifier.weight(1f),
                        )
                        SortPill(
                            text = "По использованию",
                            selected = sortByUsage,
                            onClick = { sortByUsage = true },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            items(
                items = filtered,
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
                TextButton(onClick = {
                    if (name.isNotBlank()) onRename(
                        tag.id,
                        name.trim()
                    ); editing = null
                }) { Text("Сохранить") }
            },
            dismissButton = { TextButton(onClick = { editing = null }) { Text("Отмена") } },
        )
    }
    deleting?.let { item ->
        AlertDialog(
            onDismissRequest = { deleting = null },
            title = { Text("Удалить метку «${item.tag.name}»?") },
            text = { Text("Задачи и заметки сохранятся. Метка будет удалена из связанных задач.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(item.tag.id); deleting = null
                }) { Text("Удалить", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = { TextButton(onClick = { deleting = null }) { Text("Отмена") } },
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
private fun SortPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
@Preview
fun TagsScreenContentPreview() {
    TaskManagerTheme {
        TagsScreenContent(
            items = listOf(
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
            onBackClick = {},
            onCreate = { _ -> },
            onRename = { _, _ -> },
            onDelete = {}
        )
    }
}
