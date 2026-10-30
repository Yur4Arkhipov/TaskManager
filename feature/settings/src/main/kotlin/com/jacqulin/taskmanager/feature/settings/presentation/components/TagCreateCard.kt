package com.jacqulin.taskmanager.feature.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.component.OutlinedTextField
import com.jacqulin.taskmanager.core.designsystem.component.TagChip
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagCreateCard(
    value: String,
//    tagColors: List<Color>,
//    selectedColor: Int,
//    onSelectedColorChanged: (Int) -> Unit,
    onValueChanged: (String) -> Unit,
    onCreate: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Новый тег".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            val focusManager = LocalFocusManager.current
            val focusRequester = remember { FocusRequester() }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = value,
                    placeholderText = "Например, работа",
                    onValueChange = { newName ->
                        onValueChanged(newName)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                )
                Spacer(Modifier.width(6.dp))
                CreateTagButton(
                    enabled = value.isNotBlank(),
                    onClick = {
                        onCreate(value.trim())
                        focusManager.clearFocus()
                    },
                )
            }

/*            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Цвет метки",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    tagColors.forEachIndexed { index, color ->
                        Box(
                            Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(color)
                                .then(
                                    if (selectedColor == index) Modifier.border(
                                        2.dp,
                                        MaterialTheme.colorScheme.onSurface,
                                        CircleShape
                                    ) else Modifier
                                )
                                .clickable { onSelectedColorChanged(index) },
                            contentAlignment = Alignment.Center,
                        ) {
                            if (selectedColor == index) Icon(
                                painterResource(R.drawable.ic_check),
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }*/

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Так будет выглядеть тег:",
                    style = MaterialTheme.typography.bodySmall
                )
                if (value.isNotBlank()) {
                    TagChip(
                        text = value,
                        textStyle = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                } else {
                    TagChip(
                        text = "работа",
                        textStyle = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateTagButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    val containerColor = if (enabled) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Создать",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
@Preview()
fun TagCreateCardPreview() {
    TaskManagerTheme {
        TagCreateCard(
            value = "Work",
//            tagColors = tagColors,
//            selectedColor = 0,
//            onSelectedColorChanged = { _ -> },
            onValueChanged = { _ -> },
            onCreate = { }
        )
    }
}
