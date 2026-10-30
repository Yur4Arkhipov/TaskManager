package com.jacqulin.taskmanager.feature.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.component.TagChip
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.core.model.Tag
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.settings.presentation.model.TagManagementItem
import com.jacqulin.taskmanager.feature.settings.presentation.util.pluralize

@Composable
fun TagCard(
    item: TagManagementItem,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                TagChip(
                    text = item.tag.name,
                    textStyle = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
//                        color = Color(item.tag.color.toInt())
                )
                Text(
                    text = if (item.taskCount == 0) "Пока не используется"
                        else "${item.taskCount} ${pluralize(
                            count = item.taskCount,
                            one = "задача",
                            few = "задачи",
                            many = "задач"
                        )}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopEnd)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple()
                        ) { expanded = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_more_horiz),
                        contentDescription = "Ещё",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StyledDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    StyledDropdownMenuItem(
                        text = "Переименовать",
                        onClick = {
                            expanded = false
                            onRename()
                        }
                    )
                    StyledDropdownMenuItem(
                        text = "Удалить",
                        destructive = true,
                        onClick = {
                            expanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StyledDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier.widthIn(min = 140.dp, max = 180.dp),
            shape = RoundedCornerShape(12.dp),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            tonalElevation = 3.dp,
            content = content,
        )
    }
}

@Composable
fun StyledDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    selected: Boolean = false,
    destructive: Boolean = false,
) {
    val background = if (selected) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent
    val textColor = if (destructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface

    DropdownMenuItem(
        text = {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 1.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(background)
            .heightIn(min = 32.dp),
    )
}

@Composable
@Preview
fun TagCardPreview() {
    TaskManagerTheme {
        TagCard(
            item = TagManagementItem(
                tag = Tag(
                    id = 0,
                    name = "Work"
                ),
                taskCount = 2
            ),
            onRename = { },
            onDelete = { }
        )
    }
}
