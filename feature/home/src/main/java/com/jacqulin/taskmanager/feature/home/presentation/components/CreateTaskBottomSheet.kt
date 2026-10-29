package com.jacqulin.taskmanager.feature.home.presentation.components

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
import com.jacqulin.taskmanager.core.designsystem.component.TagChip
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    title: String,
    availableTags: List<Tag>,
    selectedTags: List<Tag>,
    isPlannedForToday: Boolean,
    onTitleChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onPlanForTodayChanged: (Boolean) -> Unit,
    onTagAdded: (Tag) -> Unit,
    onTagRemoved: (Tag) -> Unit,
    onSaveTaskClick: () -> Unit,
    onNavigateToTags: () -> Unit
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
            ModalBottomSheetContent(
                title = title,
                saveButtonEnabled = title.isNotBlank(),
                availableTags = availableTags,
                selectedTags = selectedTags,
                addToTodayPlan = isPlannedForToday,
                onDismissRequest = onDismissRequest,
                onTitleChanged = onTitleChanged,
                onPlanForTodayChanged = onPlanForTodayChanged,
                onCreateTagClick = onNavigateToTags,
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
    availableTags: List<Tag>,
    selectedTags: List<Tag>,
    addToTodayPlan: Boolean,
    onDismissRequest: () -> Unit,
    onTitleChanged: (String) -> Unit,
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

        PlanForTodayRow(
            addToTodayPlan = addToTodayPlan,
            onPlanForTodayChanged = onPlanForTodayChanged
        )

        TagRow(
            availableTags = availableTags,
            selectedTags = selectedTags,
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
    availableTags: List<Tag>,
    selectedTags: List<Tag>,
    onTagAdded: (Tag) -> Unit,
    onTagRemoved: (Tag) -> Unit,
    onCreateTag: () -> Unit,
) {
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
            availableTags.forEach { tag ->
                val selected = tag in selectedTags
                TagChip(
                    text = tag.name,
                    textStyle = MaterialTheme.typography.labelMedium,
                    isSelected = selected,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .clickable {
                            if (selected) onTagRemoved(tag)
                            else onTagAdded(tag)
                        }
                )
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(6.dp),
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
            availableTags = emptyList(),
            selectedTags = emptyList(),
            addToTodayPlan = false,
            onDismissRequest = { },
            onTitleChanged = { },
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
            availableTags = listOf(Tag(id = 1, name = "Работа"), Tag(id = 2, name = "Дом"), Tag(id = 3, name = "Спорт")),
            selectedTags = listOf(Tag(id = 1, name = "Работа")),
            addToTodayPlan = false,
            onDismissRequest = { },
            onTitleChanged = { },
            onPlanForTodayChanged = { },
            onCreateTagClick = { },
            onTagAdded = { },
            onTagRemoved = { },
            onSaveTaskClick = { },
        )
    }
}
