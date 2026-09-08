package com.jacqulin.taskmanager.feature.notes.presentation.notebase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavEdge
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.FloatingActionButton
import com.jacqulin.taskmanager.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.feature.notes.navigation.NoteEditorRoute
import com.jacqulin.taskmanager.feature.notes.navigation.NotesRoute
import com.jacqulin.taskmanager.feature.notes.presentation.notebase.components.NoteItem
import com.jacqulin.taskmanager.feature.notes.presentation.notebase.components.NotesToolbar

@NavDestination(route = NotesRoute::class)
@NavEdge(to = NoteEditorRoute::class, label = "open note editor")
@Composable
fun NotesScreen(
    onAddClick: () -> Unit,
    onNoteClick: (Int) -> Unit = {},
    viewModel: NotesScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

//    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NotesEffect.NavigateToCreateNote -> onAddClick()
                is NotesEffect.NavigateToExistingNote -> onNoteClick(effect.noteId)
            }
        }
    }

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
                    viewModel.onEvent(NotesEvent.OnCreateNoteClicked)
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.notes_count,
                    uiState.visibleNotes.size
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            NotesToolbar(
                searchQuery = uiState.searchQueryInput,
                isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                onSearchQueryChanged = { query ->
                    viewModel.onEvent(NotesEvent.OnSearchQueryChanged(query))
                },
                onSearch = {
                    viewModel.onEvent(NotesEvent.OnSearchSubmitted)
                },
                onSortChanged = { sortType ->
                    viewModel.onEvent(
                        NotesEvent.OnSortChanged(sortType)
                    )
                },
                onDeleteModeClick = {
                    viewModel.onEvent(NotesEvent.OnDeleteModeToggled)
                }
            )

            if (uiState.isEmpty) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_note),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = stringResource(R.string.notes_empty))
                    }
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
                        NoteItem(
                            note = note,
                            isDeleteModeEnabled = uiState.isDeleteModeEnabled,
                            onDeleteClick = {
                                viewModel.onEvent(NotesEvent.OnDeleteNoteClicked(note.id))
                            },
                            onNoteClick = {
                                viewModel.onEvent(NotesEvent.OnNoteClicked(note.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@NavPreview(route = NotesRoute::class, primary = true)
@Preview
@Composable
fun NotesScreenPreview() {
    NotesScreen(
        onAddClick = {},
        onNoteClick = {}
    )
}
