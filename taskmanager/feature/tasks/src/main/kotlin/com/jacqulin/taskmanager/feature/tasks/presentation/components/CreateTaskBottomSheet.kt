package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    title: String,
    onTitleChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onTimeEstimateSelected: (String) -> Unit,
    onPlanForTodayChanged: (Boolean) -> Unit,
    onAddTagClick: () -> Unit,
    onSaveTaskClick: () -> Unit,
) {
    var selectedEstimate by rememberSaveable { mutableStateOf("none") }
    var addToToday by rememberSaveable { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = colors.surfaceContainerLow,
            dragHandle = {
                BottomSheetDefaults.DragHandle(color = colors.outlineVariant)
            },
        ) {
            ModalBottomSheetContent(
                title = title,
                saveButtonEnabled = title.isNotBlank(),
                selectedEstimate = selectedEstimate,
                addToTodayPlan = addToToday,
                onDismissRequest = onDismissRequest,
                onTitleChanged = onTitleChanged,
                onTimeEstimateSelected = onTimeEstimateSelected,
                onPlanForTodayChanged = onPlanForTodayChanged,
                onAddTagClick = onAddTagClick,
                onSaveTaskClick = onSaveTaskClick,
            )
        }
    }
}
@Composable
fun ModalBottomSheetContent(
    title: String,
    saveButtonEnabled: Boolean,
    selectedEstimate: String,
    addToTodayPlan: Boolean,
    onDismissRequest: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onTimeEstimateSelected: (String) -> Unit,
    onPlanForTodayChanged: (Boolean) -> Unit,
    onAddTagClick: () -> Unit,
    onSaveTaskClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TitleRow(
            onDismissRequest = onDismissRequest
        )

        TextRow(
            text = title,
            onTextChanged = onTitleChanged
        )

//        EstimateTimeRow(
//            selectedEstimate = selectedEstimate,
//            onTimeEstimateSelected = onTimeEstimateSelected
//        )

//        PlanForTodayRow(
//            addToTodayPlan = addToTodayPlan,
//            onPlanForTodayChanged = onPlanForTodayChanged
//        )

//        TagRow(
//            onAddTagClick = onAddTagClick,
//        )

        Button(
            onClick = onSaveTaskClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = saveButtonEnabled
        ) {
            Text(
                text = stringResource(R.string.tasks_add_task_submit),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun TitleRow(
    onDismissRequest: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.tasks_new_task),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        FilledTonalIconButton(
            onClick = onDismissRequest,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.tasks_close_add_menu),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun TextRow(
    text: String,
    onTextChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.tasks_task_name),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text ->
                onTextChanged(text)
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.tasks_task_title_placeholder),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
private fun EstimateTimeRow(
    selectedEstimate: String,
    onTimeEstimateSelected: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.tasks_time_estimate),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.tasks_optional),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            EstimateChip(
                text = stringResource(R.string.tasks_no_estimate),
                selected = selectedEstimate == "none",
                onClick = {
                    onTimeEstimateSelected("none")
                },
            )
            EstimateChip(
                text = stringResource(R.string.tasks_15_minutes),
                selected = selectedEstimate == "15",
                onClick = {
                    onTimeEstimateSelected("15")
                },
            )
            EstimateChip(
                text = stringResource(R.string.tasks_25_minutes),
                selected = selectedEstimate == "25",
                onClick = {
                    onTimeEstimateSelected("25")
                },
            )
            EstimateChip(
                text = stringResource(R.string.tasks_45_minutes),
                selected = selectedEstimate == "45",
                onClick = {
                    onTimeEstimateSelected("45")
                },
            )
            EstimateChip(
                text = stringResource(R.string.tasks_1_hour),
                selected = selectedEstimate == "60",
                onClick = {
                    onTimeEstimateSelected("60")
                },
            )
        }

            EstimateChip(
                text = stringResource(R.string.tasks_other_duration),
                selected = selectedEstimate == "other",
                onClick = {
                    onTimeEstimateSelected("other")
                },
            )

        Text(
            text = stringResource(R.string.tasks_time_estimate_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun EstimateChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(50)
    Text(
        text = text,
        modifier = Modifier
            .clip(shape)
            .background(if (selected) colors.primaryContainer else colors.surfaceContainer)
            .clickable(
                indication = null,
                interactionSource = null,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(horizontal = 10.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
        color = if (selected) colors.onPrimaryContainer else colors.onSurfaceVariant,
    )
}

@Composable
private fun PlanForTodayRow(
    addToTodayPlan: Boolean,
    onPlanForTodayChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(
                indication = null,
                interactionSource = null,
            ) {
                onPlanForTodayChanged(addToTodayPlan)
            }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = addToTodayPlan,
            onCheckedChange = {
                onPlanForTodayChanged(it)
            },
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = stringResource(R.string.tasks_add_to_today_plan),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun TagRow(
    onAddTagClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.tasks_tags),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.tasks_optional),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                .background(Color.Transparent)
                .clickable(
                    indication = null,
                    interactionSource = null,
                ) { onAddTagClick() }
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_label),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.tasks_add_tag),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(R.string.tasks_add),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun ModalBottomSheetContentPreview() {
    TaskManagerTheme(
        colorPalette = ColorPalette.BLUE
    ) {
        ModalBottomSheetContent(
            title = "title",
            saveButtonEnabled = true,
            selectedEstimate = "none",
            addToTodayPlan = false,
            onDismissRequest = { },
            onTitleChanged = { },
            onTimeEstimateSelected = { },
            onPlanForTodayChanged = { },
            onAddTagClick = { },
            onSaveTaskClick = { },
        )
    }
}
