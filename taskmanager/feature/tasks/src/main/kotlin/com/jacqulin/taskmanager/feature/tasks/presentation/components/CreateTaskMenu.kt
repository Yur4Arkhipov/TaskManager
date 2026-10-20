package com.jacqulin.taskmanager.feature.tasks.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun CreateTaskMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onVoiceClick: () -> Unit,
    onTextClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn(tween(180)) + slideInVertically(tween(220)) { it / 3 },
        exit = fadeOut(tween(120)) + slideOutVertically(tween(160)) { it / 4 },
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 12.dp),
        ) {
            CreateTaskMenuItem(
                iconRes = R.drawable.ic_note_edit,
                text = stringResource(R.string.tasks_add_task_thought),
                emphasized = true,
                onClick = {},
            )

            CreateTaskMenuItem(
                iconRes = R.drawable.ic_microphone,
                text = stringResource(R.string.tasks_add_task_voice),
                onClick = {
                    onDismissRequest()
                    onVoiceClick()
                },
            )

            CreateTaskMenuItem(
                iconRes = R.drawable.ic_add,
                text = stringResource(R.string.tasks_add_task_text),
                onClick = {
                    onDismissRequest()
                    onTextClick()
                },
            )
        }
    }
}

@Composable
private fun CreateTaskMenuItem(
    iconRes: Int,
    text: String,
    emphasized: Boolean = false,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(50)
    val colors = MaterialTheme.colorScheme
    val containerColor = if (emphasized) colors.primaryContainer else colors.surfaceContainer
    val contentColor = if (emphasized) colors.onPrimaryContainer else colors.onSurface

    Row(
        modifier = Modifier
            .clip(shape)
            .background(containerColor, shape)
            .then(
                if (emphasized) Modifier
                else Modifier.border(1.dp, colors.outlineVariant, shape)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
