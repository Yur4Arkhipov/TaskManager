package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun CreateTaskMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onVoiceClick: () -> Unit,
    onTextClick: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    val itemShape = RoundedCornerShape(12.dp)

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        shape = shape,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.background(Color.Transparent),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .widthIn(min = 100.dp, max = 140.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            CreateTaskMenuItem(
                iconRes = R.drawable.ic_microphone,
                text = stringResource(R.string.tasks_add_task_voice),
                shape = itemShape,
                onClick = {
                    onDismissRequest()
                    onVoiceClick()
                }
            )

            CreateTaskMenuItem(
                iconRes = R.drawable.ic_text,
                text = stringResource(R.string.tasks_add_task_text),
                shape = itemShape,
                onClick = {
                    onDismissRequest()
                    onTextClick()
                }
            )
        }
    }
}

@Composable
private fun CreateTaskMenuItem(
    @DrawableRes iconRes: Int,
    text: String,
    shape: Shape,
    onClick: () -> Unit,
) {
    val borderColor = MaterialTheme.colorScheme.outlineVariant
    val containerColor = MaterialTheme.colorScheme.surfaceContainer
    val contentColor = MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(containerColor, shape)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
