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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.component.FloatingActionButton
import com.jacqulin.taskmanager.core.designsystem.component.TopAppBar
import com.jacqulin.taskmanager.core.designsystem.component.VoiceErrorOverlay
import com.jacqulin.taskmanager.core.designsystem.component.VoiceProcessingOverlay
import com.jacqulin.taskmanager.core.designsystem.component.VoiceRecordingOverlay
import com.jacqulin.taskmanager.core.designsystem.component.VoiceSuccessOverlay
import com.jacqulin.taskmanager.core.designsystem.extensions.fadeScrollableVerticalEdges
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.tasks.presentation.components.CreateTaskBottomSheet
import com.jacqulin.taskmanager.feature.tasks.presentation.components.CreateTaskMenu
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TaskItem
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TasksFilter
import com.jacqulin.taskmanager.feature.tasks.presentation.components.TasksToolbar
import com.jacqulin.taskmanager.feature.tasks.presentation.model.MainScreenState
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskFilter
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TaskItemUi
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TasksEffect
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TasksEvent
import com.jacqulin.taskmanager.feature.tasks.presentation.model.TasksUiState

@Composable
fun TasksScreen(
    viewModel: TasksScreenViewModel = hiltViewModel(),
    onNavigateToTags: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
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
        onCreateTaskMenuToggle = { isCreateMenuExpanded = !isCreateMenuExpanded },
        onCreateTaskMenuDismissed = { isCreateMenuExpanded = false },
        onNavigateToTags = onNavigateToTags,
        onEvent = viewModel::onEvent,
    )
}


@Composable
fun TasksScreen(
    uiState: TasksUiState,
    modifier: Modifier = Modifier,
    isCreateMenuExpanded: Boolean,
    onCreateTaskMenuToggle: () -> Unit,
    onCreateTaskMenuDismissed: () -> Unit,
    onNavigateToTags: () -> Unit,
    onEvent: (TasksEvent) -> Unit,
) {
    val taskListState = rememberLazyListState()

    Scaffold(
        topBar = { TopAppBar(titleRes = R.string.tasks_title) },
        floatingActionButton = {
            if (!uiState.mainScreenState.isDeleteModeEnabled) {
                val iconRotation by animateFloatAsState(
                    targetValue = if (isCreateMenuExpanded) 45f else 0f,
                    animationSpec = tween(durationMillis = 220),
                    label = "createTaskFabIconRotation",
                )

                Column(horizontalAlignment = Alignment.End) {
                    CreateTaskMenu(
                        expanded = isCreateMenuExpanded,
                        onDismissRequest = onCreateTaskMenuDismissed,
                        onVoiceClick = { onEvent(TasksEvent.OnCreateTaskByVoiceClicked) },
                        onTextClick = { onEvent(TasksEvent.OnCreateTaskByTextClicked) }
                    )
                    FloatingActionButton(
                        icon = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(
                            if (isCreateMenuExpanded) R.string.tasks_close_add_menu else R.string.tasks_add_task
                        ),
                        onClick = onCreateTaskMenuToggle,
                        iconRotationDegrees = iconRotation,
                    )
                }
            }
        },
        bottomBar = {
            if (uiState.mainScreenState.isDeleteModeEnabled) {
                BottomBar(
                    onClick = { onEvent(TasksEvent.OnDeleteSelectedTasks) },
                    enabled = uiState.mainScreenState.selectedTaskIds.isNotEmpty(),
                    selectedSize = uiState.mainScreenState.selectedTaskIds.size
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
                isDeleteModeEnabled = uiState.mainScreenState.isDeleteModeEnabled,
                selectedCount = uiState.mainScreenState.selectedTaskIds.size,
                allSelected = uiState.visibleTasks.isNotEmpty()
                        && uiState.mainScreenState.selectedTaskIds.containsAll(uiState.visibleTasks.map { it.id }),
                sortType = uiState.mainScreenState.sortType,
                showCreationDate = uiState.mainScreenState.showCreationDate,
                showTags = uiState.mainScreenState.showTags,
                onSearchQueryChanged = { query ->
                    onEvent(TasksEvent.OnSearchQueryChanged(query))
                },
                onSortChanged = { sortType ->
                    onEvent(TasksEvent.OnSortChanged(sortType))
                },
                onDeleteModeClick = { onEvent(TasksEvent.OnDeleteModeToggled) },
                onSelectAllClick = { onEvent(TasksEvent.OnSelectAllTasks) },
                onShowCreationDateChanged = { isChanged ->
                    onEvent(TasksEvent.OnShowCreationDateChanged(isChanged))
                },
                onShowTagsChanged = { isChanged ->
                    onEvent(TasksEvent.OnShowTagsChanged(isChanged))
                },
            )

            if (!uiState.mainScreenState.isDeleteModeEnabled) {
                TasksFilter(
                    taskFilter = uiState.mainScreenState.filter,
                    onTaskFilterChanged = { filter ->
                        onEvent(TasksEvent.OnTaskFilterChanged(filter))
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
                    state = taskListState,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .fadeScrollableVerticalEdges(taskListState)
                ) {
                    items(uiState.visibleTasks) { task ->
                        TaskItem(
                            task = task,
                            isDeleteModeEnabled = uiState.mainScreenState.isDeleteModeEnabled,
                            isSelected = task.id in uiState.mainScreenState.selectedTaskIds,
                            showCreationDate = uiState.mainScreenState.showCreationDate,
                            showTags = uiState.mainScreenState.showTags,
                            onSelectionClick = { onEvent(TasksEvent.OnTaskSelectionToggled(task.id)) },
                            onLongPress = {
                                if (!uiState.mainScreenState.isDeleteModeEnabled)
                                    onEvent(TasksEvent.OnEnterDeleteMode(task.id))
                            },
                            onDeleteClick = { onEvent(TasksEvent.OnDeleteTaskClicked(task.id)) },
                            onCompleteClick = { onEvent(TasksEvent.UpdateTaskStatus(task)) },
                        )
                    }
                }
            }
        }
    }

    if (uiState.mainScreenState.isBottomSheetVisible) {
        CreateTaskBottomSheet(
            title = uiState.taskState.title,
            availableTags = uiState.availableTags,
            selectedTags = uiState.taskState.tags,
            isPlannedForToday = uiState.taskState.isPlannedForToday,
            onDismissRequest = {
                onEvent(TasksEvent.OnBottomSheetDismissed)
            },
            onTitleChanged = { title ->
                onEvent(TasksEvent.OnTaskTextChanged(title))
            },
            onTimeEstimateSelected = { estimate ->
                onEvent(TasksEvent.OnTaskTimeEstimateSelected(estimate))
            },
            onPlanForTodayChanged = { enabled ->
                onEvent(TasksEvent.OnTaskPlanForTodayChanged(enabled))
            },
            onTagAdded = { tag ->
                onEvent(TasksEvent.OnTaskTagAdded(tag))
            },
            onTagRemoved = { tag ->
                onEvent(TasksEvent.OnTaskTagRemoved(tag))
            },
            onSaveTaskClick = {
                onEvent(TasksEvent.OnSaveTaskClicked)
            },
            onNavigateToTags = onNavigateToTags
        )
    }

    when (val voiceState = uiState.voiceState) {
        VoiceState.Idle -> Unit

        is VoiceState.Recording -> {
            VoiceRecordingOverlay(
                partialText = voiceState.partialText,
                onStopClick = { onEvent(TasksEvent.VoiceInputStopClicked) }
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
                onRetry = { onEvent(TasksEvent.VoiceInputRetry) },
                onCancel = { onEvent(TasksEvent.VoiceInputCancel) }
            )
        }
    }
}

@Composable
private fun BottomBar(
    onClick: () -> Unit,
    enabled: Boolean,
    selectedSize: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
            modifier = Modifier.height(36.dp),
        ) {
            Text(
                text = stringResource(
                    R.string.tasks_delete_selected,
                    selectedSize,
                ),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    TaskManagerTheme {
        BottomBar(
            onClick = {},
            enabled = true,
            selectedSize = 1
        )
    }
}

@Preview
@Composable
fun TasksScreenContentPreview_DeleteMode() {
    TaskManagerTheme {
        TasksScreen(
            uiState = TasksUiState(
                mainScreenState = MainScreenState(
                    isDeleteModeEnabled = true
                ),
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
            onCreateTaskMenuToggle = {},
            onCreateTaskMenuDismissed = {},
            onNavigateToTags = {},
            onEvent = { },
        )
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
            onCreateTaskMenuToggle = {},
            onCreateTaskMenuDismissed = {},
            onNavigateToTags = {},
            onEvent = { },
        )
    }
}
