package com.jacqulin.taskmanager.feature.tasks.presentation

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
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
import com.jacqulin.taskmanager.core.designsystem.component.FloatingActionButton
import com.jacqulin.taskmanager.core.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.core.designsystem.component.VoiceErrorOverlay
import com.jacqulin.taskmanager.core.designsystem.component.VoiceProcessingOverlay
import com.jacqulin.taskmanager.core.designsystem.component.VoiceRecordingOverlay
import com.jacqulin.taskmanager.core.designsystem.component.VoiceSuccessOverlay
import com.jacqulin.taskmanager.core.designsystem.model.SortType
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.tasks.presentation.components.CreateTaskMenu
import com.jacqulin.taskmanager.feature.tasks.presentation.components.CreateTaskBottomSheet
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TaskItem
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TasksFilter
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TasksToolbar
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi

@Composable
fun TasksScreen(
    viewModel: TasksScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    val context = LocalContext.current

    var isCreateMenuExpanded by rememberSaveable { mutableStateOf(false) }
    
    val voicePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.d("TasksScreen", "Voice permission granted")
            viewModel.onEvent(TasksEvent.VoicePermissionGranted)
        } else {
            Log.d("TasksScreen", "Voice permission denied")
            viewModel.onEvent(TasksEvent.VoicePermissionDenied)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                TasksEffect.RequestVoicePermission -> {
                    Log.d("TasksScreen", "TasksEffect.RequestVoicePermission called")
                    voicePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                is TasksEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    TasksScreen(
        uiState = uiState,
        isCreateMenuExpanded = isCreateMenuExpanded,
        onSearchQueryChanged = { query ->
            viewModel.onEvent(TasksEvent.OnSearchQueryChanged(query))
        },
        onSortChanged = { sortType ->
            viewModel.onEvent(TasksEvent.OnSortChanged(sortType))
        },
        onTaskFilterChanged = { filter ->
            viewModel.onEvent(TasksEvent.OnTaskFilterChanged(filter))
        },
        onTaskDeleted = { taskId ->
            viewModel.onEvent(TasksEvent.OnDeleteTaskClicked(taskId))
        },
        onTaskStatusUpdated = { task ->
            viewModel.onEvent(TasksEvent.UpdateTaskStatus(task))
        },
        onCreateTaskMenuToggle = { isCreateMenuExpanded = !isCreateMenuExpanded },
        onCreateTaskMenuDismissed = { isCreateMenuExpanded = false },
        onTextTaskSheetEvent = viewModel::onEvent,
        onTextTaskClick = {
            viewModel.onEvent(TasksEvent.OnCreateTaskByTextClicked)
        },
        onVoiceTaskClick = {
            viewModel.onEvent(TasksEvent.OnCreateTaskByVoiceClicked)
        },
        onVoiceInputStopClicked = {
            viewModel.onEvent(TasksEvent.VoiceInputStopClicked)
        },
        onVoiceInputRetry = {
            viewModel.onEvent(TasksEvent.VoiceInputRetry)
        },
        onVoiceInputCancel = {
            viewModel.onEvent(TasksEvent.VoiceInputCancel)
        },
    )
}


@Composable
fun TasksScreen(
    uiState: TasksUiState,
    isCreateMenuExpanded: Boolean,
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit,
    onSortChanged: (SortType) -> Unit,
    onTaskFilterChanged: (TaskFilter) -> Unit,
    onTaskDeleted: (Int) -> Unit,
    onTaskStatusUpdated: (TaskItemUi) -> Unit,
    onCreateTaskMenuToggle: () -> Unit,
    onCreateTaskMenuDismissed: () -> Unit,
    onVoiceTaskClick: () -> Unit,
    onTextTaskClick: () -> Unit,
    onVoiceInputStopClicked: () -> Unit,
    onVoiceInputRetry: () -> Unit,
    onVoiceInputCancel: () -> Unit,
    onTextTaskSheetEvent: (TasksEvent) -> Unit = {},
) {
    Scaffold(
        topBar = { TopAppBar(titleRes = R.string.tasks_title) },
        floatingActionButton = {
            val iconRotation by animateFloatAsState(
                targetValue = if (isCreateMenuExpanded) 45f else 0f,
                animationSpec = tween(durationMillis = 220),
                label = "createTaskFabIconRotation",
            )

            Column(horizontalAlignment = Alignment.End) {
                CreateTaskMenu(
                    expanded = isCreateMenuExpanded,
                    onDismissRequest = onCreateTaskMenuDismissed,
                    onVoiceClick = onVoiceTaskClick,
                    onTextClick = onTextTaskClick
                )

                FloatingActionButton(
                    icon = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(
                        if (isCreateMenuExpanded) R.string.tasks_close_add_menu
                        else R.string.tasks_add_task
                    ),
                    onClick = onCreateTaskMenuToggle,
                    iconRotationDegrees = iconRotation,
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            TasksToolbar(
                searchQuery = uiState.mainScreenState.searchQuery,
                sortType = uiState.mainScreenState.sortType,
                onSearchQueryChanged = onSearchQueryChanged,
                onSortChanged = onSortChanged
            )

            TasksFilter(
                taskFilter = uiState.mainScreenState.filter,
                onTaskFilterChanged = onTaskFilterChanged,
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
                            painter = painterResource(R.drawable.ic_task),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = when {
                                uiState.mainScreenState.searchQuery.isNotBlank() -> {
                                    stringResource(R.string.tasks_search_no_results)
                                }
                                uiState.mainScreenState.filter == TaskFilter.ACTIVE -> {
                                    stringResource(R.string.tasks_no_active_tasks)
                                }
                                uiState.mainScreenState.filter == TaskFilter.COMPLETED -> {
                                    stringResource(R.string.tasks_no_completed_tasks)
                                }
                                else -> stringResource(R.string.tasks_no_tasks_yet)
                            }
                        )
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
                            onDeleteClick = { onTaskDeleted(task.id) },
                            onCompleteClick = { onTaskStatusUpdated(task) }
                        )
                    }
                }
            }
        }
    }

    if (uiState.mainScreenState.isBottomSheetVisible) {
        CreateTaskBottomSheet(
            title = uiState.taskState.title,
            onDismissRequest = {
                onTextTaskSheetEvent(TasksEvent.OnBottomSheetDismissed)
            },
            onTitleChanged = { title ->
                onTextTaskSheetEvent(TasksEvent.OnTaskTextChanged(title))
            },
            onTimeEstimateSelected = { estimate ->
                onTextTaskSheetEvent(TasksEvent.OnTaskTimeEstimateSelected(estimate))
            },
            onPlanForTodayChanged = { enabled ->
                onTextTaskSheetEvent(TasksEvent.OnTaskPlanForTodayChanged(enabled))
            },
            onAddTagClick = {
                onTextTaskSheetEvent(TasksEvent.OnTaskAddTagClicked)
            },
            onSaveTaskClick = {
                onTextTaskSheetEvent(TasksEvent.OnSaveTaskClicked)
            },
        )
    }

    when (val voiceState = uiState.voiceState) {
        VoiceState.Idle -> Unit

        is VoiceState.Recording -> {
            VoiceRecordingOverlay(
                partialText = voiceState.partialText,
                onStopClick = onVoiceInputStopClicked
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
                    voiceState.text
                )
            )
        }

        is VoiceState.Error -> {
            VoiceErrorOverlay(
                errorMessage = when (voiceState.error) {
                    VoiceError.RecordingFailed -> stringResource(R.string.voice_error_recording_failed)
                    VoiceError.RecognitionFailed -> stringResource(R.string.voice_error_recognition_failed)
                    VoiceError.Network -> stringResource(R.string.voice_error_network)
                    VoiceError.Unauthorized -> stringResource(R.string.voice_error_unauthorized)
                    VoiceError.EmptyResult -> stringResource(R.string.voice_error_empty_result)
                    VoiceError.Unknown -> stringResource(R.string.tasks_voice_unknown_error)
                },
                onRetry = onVoiceInputRetry,
                onCancel = onVoiceInputCancel
            )
        }
    }
}

@Preview
@Composable
fun TasksScreenContentPreview() {
    TaskManagerTheme {
        TasksScreen(
            uiState = TasksUiState(
                visibleTasks = listOf(
                    TaskItemUi(
                        id = 1,
                        title = "Buy groceries",
                        createdAtMillis = System.currentTimeMillis(),
                        isCompleted = false
                    ),
                    TaskItemUi(
                        id = 2,
                        title = "Call John",
                        createdAtMillis = System.currentTimeMillis() - 86400000,
                        isCompleted = true
                    )
                ),
            ),
            isCreateMenuExpanded = false,
            onSearchQueryChanged = {},
            onSortChanged = {},
            onTaskFilterChanged = {},
            onTaskDeleted = {},
            onTaskStatusUpdated = {},
            onCreateTaskMenuToggle = {},
            onCreateTaskMenuDismissed = {},
            onVoiceTaskClick = {},
            onTextTaskClick = {},
            onVoiceInputStopClicked = {},
            onVoiceInputRetry = {},
            onVoiceInputCancel = {}
        )
    }
}
