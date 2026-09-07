package com.jacqulin.taskmanager.feature.tasks.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TaskItem
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TasksToolbar
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi

@NavDestination(route = TasksRoute::class)
@Composable
fun TasksScreen(
    viewModel: TasksScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var isCreateMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                titleRes = R.string.tasks_title
            )
        },
        floatingActionButton = {
            Box {
                DropdownMenu(
                    expanded = isCreateMenuExpanded,
                    onDismissRequest = {
                        isCreateMenuExpanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Голосом")
                        },
                        onClick = {
                            isCreateMenuExpanded = false

                            viewModel.onEvent(
                                TasksEvent.OnCreateTaskByVoiceClicked
                            )
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text("Текстом")
                        },
                        onClick = {
                            isCreateMenuExpanded = false

                            viewModel.onEvent(
                                TasksEvent.OnCreateTaskByTextClicked
                            )
                        }
                    )
                }

                FloatingActionButton(
                    icon = painterResource(R.drawable.ic_note_edit),
                    contentDescription = stringResource(
                        R.string.tasks_add_task
                    ),
                    onClick = {
                        isCreateMenuExpanded = !isCreateMenuExpanded
                    }
                )
            }
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
//                text = "${uiState.visibleTasks.size} заметок",
                text = "1 активных * 0 выполнено",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TasksToolbar(
                searchQuery = uiState.searchQueryInput,
                onSearchQueryChanged = { query ->
                    viewModel.onEvent(TasksEvent.OnSearchQueryChanged(query))
                },
                onSearch = {
                    viewModel.onEvent(TasksEvent.OnSearchSubmitted)
                },
                onSortChanged = { sortType ->
                    viewModel.onEvent(TasksEvent.OnSortChanged(sortType))
                }
            )

            if (uiState.draftTask != null) {
                TaskItem(
                    task = TaskItemUi(
                        id = -1,
                        title = "",
                        createdAtMillis = 0,
                        isCompleted = false,
                    ),
                    isEditing = true,
                    editingText = uiState.draftTask!!.title,
                    onEditingTextChanged = {
                        viewModel.onEvent(
                            TasksEvent.OnDraftTaskTextChanged(it)
                        )
                    },
                    onSaveClick = {
                        viewModel.onEvent(
                            TasksEvent.OnDraftTaskSaveClicked
                        )
                    },
                    onDeleteClick = {
                        viewModel.onEvent(
                            TasksEvent.OnDraftTaskDeleteClicked
                        )
                    },
                    onCompleteClick = {

                    }
                )
            }

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
                            painter = painterResource(R.drawable.ic_task),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = "Задач пока нет")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.visibleTasks) { task ->
                        TaskItem(
                            task = task,
                            onDeleteClick = {
//                                viewModel.onEvent(NotesEvent.OnDeleteNoteClicked(note.id))
                            },
                            onCompleteClick = {
//                                viewModel.onEvent(NotesEvent.OnNoteClicked(note.id))
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
