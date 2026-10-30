package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.core.designsystem.component.RoundedCheckbox
import com.jacqulin.taskmanager.core.designsystem.component.TitleRow
import com.jacqulin.taskmanager.core.designsystem.extensions.fadeScrollableVerticalEdges
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.model.HomeTaskUi
import com.jacqulin.taskmanager.feature.home.presentation.model.MainState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTasksForPlanBottomSheet(
    state: MainState,
    onTaskSelectionChange: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveClick: (Set<Int>) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            dragHandle = {
                BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.outlineVariant)
            },
            sheetGesturesEnabled = false,
        ) {
            AddTasksForPlanContent(
                tasks = state.availableTasksForPlan,
                selectedTaskIds = state.selectedTaskIdsForPlan,
                onTaskSelectionChange = onTaskSelectionChange,
                onDismissRequest = onDismissRequest,
                onSaveTasksClick = onSaveClick,
            )
        }
    }
}

@Composable
private fun AddTasksForPlanContent(
    tasks: List<HomeTaskUi>,
    selectedTaskIds: Set<Int>,
    onTaskSelectionChange: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onSaveTasksClick: (Set<Int>) -> Unit,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TitleRow(
            title = stringResource(R.string.home_select_tasks_title),
            iconDescription = stringResource(R.string.tasks_close_menu),
            onDismissRequest = onDismissRequest
        )

        if (tasks.isNotEmpty()) {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
                    .fadeScrollableVerticalEdges(listState)
            ) {
                items(
                    items = tasks,
                    key = { it.id }
                ) { task ->
                    TaskSelectionRow(
                        task = task,
                        isSelected = task.id in selectedTaskIds,
                        onSelectedChange = {
                            onTaskSelectionChange(task.id)
                        },
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_select_tasks_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(
            onClick = { onSaveTasksClick(selectedTaskIds) } ,
            enabled = selectedTaskIds.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(.85f),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = stringResource(R.string.home_select_tasks_add, selectedTaskIds.size),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TaskSelectionRow(
    task: HomeTaskUi,
    isSelected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(60.dp)
                .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = { onSelectedChange(!isSelected) }),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            RoundedCheckbox(
                checked = isSelected,
                onCheckedChange = { checked -> onSelectedChange(checked) },
                cornerRadius = 6.dp,
            )

            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(if (isSelected) 1f else .8f)
            )
        }
    }
}

@Preview
@Composable
private fun AddTasksForPlanContentPreview() {
    TaskManagerTheme {
        AddTasksForPlanContent(
            tasks = listOf(
                HomeTaskUi(id = 1, title = "Подготовить презентацию", isPlannedForToday = false),
                HomeTaskUi(id = 2, title = "Провести код-ревью", isPlannedForToday = false),
                HomeTaskUi(id = 3, title = "Созвон с командой", isPlannedForToday = true),
                HomeTaskUi(id = 4, title = "Обновить документацию", isPlannedForToday = false),
            ),
            selectedTaskIds = emptySet(),
            onDismissRequest = {},
            onTaskSelectionChange = {},
            onSaveTasksClick = { },
        )
    }
}
