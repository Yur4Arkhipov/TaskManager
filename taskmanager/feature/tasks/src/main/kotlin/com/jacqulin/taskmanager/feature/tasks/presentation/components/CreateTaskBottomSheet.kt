package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.component.RoundedCheckbox
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    title: String,
    tags: List<Tag>,
    isPlannedForToday: Boolean,
    onTitleChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onTimeEstimateSelected: (String) -> Unit,
    onPlanForTodayChanged: (Boolean) -> Unit,
    onTagAdded: (Tag) -> Unit,
    onTagRemoved: (Tag) -> Unit,
    onSaveTaskClick: () -> Unit,
) {
    var selectedEstimate by rememberSaveable { mutableStateOf("none") }
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
                tags = tags,
                addToTodayPlan = isPlannedForToday,
                onDismissRequest = onDismissRequest,
                onTitleChanged = onTitleChanged,
                onTimeEstimateSelected = onTimeEstimateSelected,
                onPlanForTodayChanged = onPlanForTodayChanged,
                onCreateTagClick = { },
                onTagAdded = onTagAdded,
                onTagRemoved = onTagRemoved,
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
    tags: List<Tag>,
    addToTodayPlan: Boolean,
    onDismissRequest: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onTimeEstimateSelected: (String) -> Unit,
    onPlanForTodayChanged: (Boolean) -> Unit,
    onCreateTagClick: () -> Unit,
    onTagAdded: (Tag) -> Unit,
    onTagRemoved: (Tag) -> Unit,
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

        PlanForTodayRow(
            addToTodayPlan = addToTodayPlan,
            onPlanForTodayChanged = onPlanForTodayChanged
        )

        TagRow(
            tags = tags,
            onTagAdded = onTagAdded,
            onTagRemoved = onTagRemoved,
            onCreateTag = onCreateTagClick
        )

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
            modifier = Modifier.size(36.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
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

//@Composable
//private fun EstimateTimeRow(
//    selectedEstimate: String,
//    onTimeEstimateSelected: (String) -> Unit,
//) {
//    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = stringResource(R.string.tasks_time_estimate),
//                style = MaterialTheme.typography.labelMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//            )
//            Text(
//                text = stringResource(R.string.tasks_optional),
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//            )
//        }
//
//        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//            EstimateChip(
//                text = stringResource(R.string.tasks_no_estimate),
//                selected = selectedEstimate == "none",
//                onClick = {
//                    onTimeEstimateSelected("none")
//                },
//            )
//            EstimateChip(
//                text = stringResource(R.string.tasks_15_minutes),
//                selected = selectedEstimate == "15",
//                onClick = {
//                    onTimeEstimateSelected("15")
//                },
//            )
//            EstimateChip(
//                text = stringResource(R.string.tasks_25_minutes),
//                selected = selectedEstimate == "25",
//                onClick = {
//                    onTimeEstimateSelected("25")
//                },
//            )
//            EstimateChip(
//                text = stringResource(R.string.tasks_45_minutes),
//                selected = selectedEstimate == "45",
//                onClick = {
//                    onTimeEstimateSelected("45")
//                },
//            )
//            EstimateChip(
//                text = stringResource(R.string.tasks_1_hour),
//                selected = selectedEstimate == "60",
//                onClick = {
//                    onTimeEstimateSelected("60")
//                },
//            )
//        }
//
//            EstimateChip(
//                text = stringResource(R.string.tasks_other_duration),
//                selected = selectedEstimate == "other",
//                onClick = {
//                    onTimeEstimateSelected("other")
//                },
//            )
//
//        Text(
//            text = stringResource(R.string.tasks_time_estimate_hint),
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant,
//        )
//    }
//}
//
//@Composable
//private fun EstimateChip(
//    text: String,
//    selected: Boolean,
//    onClick: () -> Unit,
//) {
//    val colors = MaterialTheme.colorScheme
//    val shape = RoundedCornerShape(50)
//    Text(
//        text = text,
//        modifier = Modifier
//            .clip(shape)
//            .background(if (selected) colors.primaryContainer else colors.surfaceContainer)
//            .clickable(
//                indication = null,
//                interactionSource = null,
//                role = Role.Button,
//                onClick = onClick,
//            )
//            .padding(horizontal = 10.dp, vertical = 8.dp),
//        style = MaterialTheme.typography.labelSmall,
//        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
//        color = if (selected) colors.onPrimaryContainer else colors.onSurfaceVariant,
//    )
//}

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
                onPlanForTodayChanged(!addToTodayPlan)
            }
            .heightIn(min = 56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RoundedCheckbox(
            checked = addToTodayPlan,
            onCheckedChange = onPlanForTodayChanged
        )
        Text(
            text = stringResource(R.string.tasks_add_to_today_plan),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagRow(
    tags: List<Tag>,
    onTagAdded: (Tag) -> Unit,
    onCreateTag: () -> Unit,
    onTagRemoved: (Tag) -> Unit,
) {
    val suggestedTags = listOf("hghghf", "павпавп", "пвпвпавп", "gfgfgf", "fdsfdsfdsfdsf", "fdsf")
        .mapIndexed { index, name -> Tag(id = index + 1, name = name) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = stringResource(R.string.tasks_tags),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            (suggestedTags + tags.filterNot { selectedTag ->
                suggestedTags.any { it.name.equals(selectedTag.name, ignoreCase = true) }
            })
                .distinctBy { it.name.lowercase() }
                .forEach { tag ->
                    val selectedTag = tags.firstOrNull { it.name.equals(tag.name, ignoreCase = true) }
                    val selected = selectedTag != null
                    Text(
                        text = tag.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (selected) MaterialTheme.colorScheme.secondaryContainer
                                else MaterialTheme.colorScheme.surfaceContainerHighest,
                            )
                            .clickable {
                                selectedTag?.let(onTagRemoved) ?: onTagAdded(tag)
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                    )
                }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(50),
                    )
                    .clickable(onClick = onCreateTag)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.tasks_create_tag),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
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
            tags = emptyList(),
            onDismissRequest = { },
            onTitleChanged = { },
            onTimeEstimateSelected = { },
            onPlanForTodayChanged = { },
            onCreateTagClick = { },
            onTagAdded = { },
            onTagRemoved = { },
            onSaveTaskClick = { },
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ModalBottomSheetContentPreview_WithTags() {
    TaskManagerTheme(
        colorPalette = ColorPalette.BLUE
    ) {
        ModalBottomSheetContent(
            title = "title",
            saveButtonEnabled = true,
            selectedEstimate = "none",
            addToTodayPlan = false,
            tags = listOf(Tag(id = 1, name = "tag1"), Tag(id = 2, name = "tag2")),
            onDismissRequest = { },
            onTitleChanged = { },
            onTimeEstimateSelected = { },
            onPlanForTodayChanged = { },
            onCreateTagClick = { },
            onTagAdded = { },
            onTagRemoved = { },
            onSaveTaskClick = { },
        )
    }
}
