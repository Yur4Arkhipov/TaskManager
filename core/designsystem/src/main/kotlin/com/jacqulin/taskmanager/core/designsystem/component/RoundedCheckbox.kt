package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun RoundedCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    cornerRadius: Dp = 6.dp,
    uncheckedColor: Color = MaterialTheme.colorScheme.outlineVariant,
    checkedColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    checkmarkColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    val shape = RoundedCornerShape(cornerRadius)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .toggleable(
                value = checked,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = ripple(),
                onValueChange = onCheckedChange,
            )
            .background(
                color = if (checked) checkedColor else Color.Transparent,
                shape = shape,
            )
            .border(
                width = 1.dp,
                color = if (checked) checkedColor else uncheckedColor,
                shape = shape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(size * 0.7f),
            )
        }
    }
}

@Composable
@Preview
fun RoundedCheckboxPreview() {
    TaskManagerTheme {
        RoundedCheckbox(
            checked = true,
            onCheckedChange = {},
        )
    }
}
