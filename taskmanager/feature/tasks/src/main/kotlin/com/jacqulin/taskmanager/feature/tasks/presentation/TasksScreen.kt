package com.jacqulin.taskmanager.feature.tasks.presentation

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.FloatingActionButton
import com.jacqulin.taskmanager.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.feature.tasks.navigation.TasksRoute
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TasksToolbar

@NavDestination(route = TasksRoute::class)
@Composable
fun TasksScreen(
    onAddClick: () -> Unit,
    viewModel: TasksScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                titleRes = R.string.tasks_title
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                icon = painterResource(R.drawable.ic_note_edit),
                contentDescription = stringResource(R.string.tasks_add_task),
                onClick = {
//                    viewModel.onEvent(NotesEvent.OnCreateNoteClicked)
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
//                text = "${uiState.visibleNotes.size} заметок",
                text = "1 активных * 0 выполнено",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TasksToolbar(
                searchQuery = /*uiState.searchQueryInput*/,
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
                        Text(text = "Заметок пока нет")
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


@NavPreview(route = TasksRoute::class, primary = true)
@Preview
@Composable
fun TasksScreenPreview() {
    TasksScreen()
}
