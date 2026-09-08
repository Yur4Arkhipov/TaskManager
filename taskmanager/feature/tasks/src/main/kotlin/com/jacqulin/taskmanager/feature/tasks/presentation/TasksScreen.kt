package com.jacqulin.taskmanager.feature.tasks.presentation

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.core.voice.domain.VoiceState
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.FloatingActionButton
import com.jacqulin.taskmanager.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.feature.tasks.navigation.TasksRoute
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TaskItem
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TasksToolbar
import com.jacqulin.taskmanager.feature.tasks.presentation.components.VoiceErrorOverlay
import com.jacqulin.taskmanager.feature.tasks.presentation.components.VoiceProcessingOverlay
import com.jacqulin.taskmanager.feature.tasks.presentation.components.VoiceRecordingOverlay
import com.jacqulin.taskmanager.feature.tasks.presentation.components.VoiceSuccessOverlay
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@NavDestination(route = TasksRoute::class)
@Composable
fun TasksScreen(
    viewModel: TasksScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    var isCreateMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val activeTasksCount = uiState.visibleTasks.count { !it.isCompleted }
    val completedTasksCount = uiState.visibleTasks.count { it.isCompleted }

    val voicePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.onEvent(TasksEvent.VoicePermissionGranted)
        } else {
            viewModel.onEvent(TasksEvent.VoicePermissionDenied)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                TasksEffect.RequestVoicePermission -> {
                    voicePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                is TasksEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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
                            Text(stringResource(R.string.tasks_add_task_voice))
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
                            Text(stringResource(R.string.tasks_add_task_text))
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
                text = stringResource(
                    id = R.string.tasks_status_summary,
                    activeTasksCount,
                    completedTasksCount
                ),
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
                        Text(text = stringResource(R.string.tasks_no_tasks_yet))
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
                                viewModel.onEvent(TasksEvent.OnDeleteTaskClicked(task.id))
                            },
                            onCompleteClick = {
                                viewModel.onEvent(TasksEvent.UpdateTaskStatus(task))
                            }
                        )
                    }
                }
            }
        }
    }

    when (uiState.voiceState) {
        VoiceState.Idle -> Unit

        VoiceState.Recording -> {
            VoiceRecordingOverlay(
                onStopClick = {
                    viewModel.onEvent(TasksEvent.VoiceInputStopClicked)
                }
            )
        }

        VoiceState.Processing -> {
            VoiceProcessingOverlay(
                text = stringResource(R.string.tasks_voice_processing)
            )
        }

        is VoiceState.Success -> {
            VoiceSuccessOverlay(
                taskTitle = stringResource(
                    id = R.string.voice_recognized_text,
                   (uiState.voiceState as VoiceState.Success).text
                )
            )
            LaunchedEffect(Unit) {
                delay(2000.milliseconds)
                viewModel.onEvent(TasksEvent.VoiceInputDismissed)
            }
        }

        is VoiceState.Error -> {
            VoiceErrorOverlay(
                errorMessage = stringResource(R.string.tasks_voice_unknown_error),
                onRetry = { viewModel.onEvent(TasksEvent.VoiceInputRetry) },
                onCancel = { viewModel.onEvent(TasksEvent.VoiceInputCancel) }
            )
        }
    }
}


@NavPreview(route = TasksRoute::class, primary = true)
@Preview
@Composable
fun TasksScreenPreview() {
    TasksScreen()
}
