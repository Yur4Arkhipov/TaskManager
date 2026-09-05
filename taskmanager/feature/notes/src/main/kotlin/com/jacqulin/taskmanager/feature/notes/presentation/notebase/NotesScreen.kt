package com.jacqulin.taskmanager.feature.notes.presentation.notebase

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavEdge
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.FloatingActionButton
import com.jacqulin.taskmanager.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.feature.notes.navigation.NoteEditorRoute
import com.jacqulin.taskmanager.feature.notes.navigation.NotesRoute
import com.jacqulin.taskmanager.feature.notes.presentation.model.NoteListItemUi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@NavDestination(route = NotesRoute::class)
@NavEdge(to = NoteEditorRoute::class, label = "open note editor")
@Composable
fun NotesScreen(
    onAddClick: () -> Unit,
    onNoteClick: (String) -> Unit = {},
    viewModel: NotesScreenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NotesEffect.NavigateToCreateNote -> onAddClick()
                is NotesEffect.NavigateToExistingNote -> onNoteClick(effect.noteId)
            }
        }
    }

    NotesScreenContent(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun NotesScreenContent(
    uiState: NotesUiState,
    onAction: (NotesEvent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                titleRes = R.string.notes_title
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                icon = painterResource(R.drawable.ic_note_edit),
                contentDescription = stringResource(R.string.notes_add_note),
                onClick = {
                    onAction(NotesEvent.OnCreateNoteClicked)
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "${uiState.visibleNotes.size} заметок",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.searchQueryInput,
                    onValueChange = { onAction(NotesEvent.OnSearchQueryChanged(it)) },
                    label = { Text("Поиск") },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onSearch = {
                            keyboardController?.hide()
                            onAction(NotesEvent.OnSearchSubmitted)
                        }
                    ),
                    modifier = Modifier.weight(1f)
                )

                Box {
                    OutlinedButton(onClick = { sortMenuExpanded = true }) {
                        Text(text = "Фильтр")
                    }

                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Сначала новые") },
                            onClick = {
                                onAction(NotesEvent.OnSortChanged(NotesSortType.NEW_TO_OLD))
                                sortMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Сначала старые") },
                            onClick = {
                                onAction(NotesEvent.OnSortChanged(NotesSortType.OLD_TO_NEW))
                                sortMenuExpanded = false
                            }
                        )
                    }
                }

                OutlinedButton(onClick = { onAction(NotesEvent.OnDeleteModeToggled) }) {
                    Text(
                        text = if (uiState.isDeleteModeEnabled) {
                            "Удаление: Вкл"
                        } else {
                            "Удаление"
                        }
                    )
                }
            }

            if (uiState.isEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Заметок пока нет")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.visibleNotes,
                        key = { note -> note.id }
                    ) { note ->
                        NoteRow(
                            note = note,
                            isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                            onDeleteClick = {
                                onAction(NotesEvent.OnDeleteNoteClicked(note.id))
                            },
                            onNoteClick = {
                                onAction(NotesEvent.OnNoteClicked(note.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteRow(
    note: NoteListItemUi,
    isDeleteModeEnabled: Boolean,
    onDeleteClick: () -> Unit,
    onNoteClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDeleteModeEnabled, onClick = onNoteClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Text(text = if (note.hasPreviewImage) "IMG" else "—")
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Text(
                    text = note.createdAtMillis.toFormattedDate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isDeleteModeEnabled) {
                OutlinedButton(onClick = onDeleteClick) {
                    Text(text = "Удалить")
                }
            }
        }
    }
}

private fun Long.toFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(formatter)
}

@NavPreview(route = NotesRoute::class, primary = true)
@Preview
@Composable
fun NotesScreenPreview() {
    NotesScreenContent(
        uiState = NotesUiState(
            searchQueryInput = "Демо",
            appliedSearchQuery = "Демо",
            isDeleteModeEnabled = false,
            visibleNotes = listOf(
                NoteListItemUi(
                    id = "1",
                    title = "Демо заметка",
                    createdAtMillis = 1_756_985_600_000,
                    hasPreviewImage = false
                ),
                NoteListItemUi(
                    id = "2",
                    title = "План на неделю",
                    createdAtMillis = 1_756_899_200_000,
                    hasPreviewImage = true
                )
            ),
            isEmpty = false
        ),
        onAction = {}
    )
}
