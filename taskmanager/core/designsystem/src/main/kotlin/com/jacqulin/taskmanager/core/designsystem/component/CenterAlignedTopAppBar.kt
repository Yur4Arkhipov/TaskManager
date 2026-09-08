package com.jacqulin.taskmanager.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    navigationIcon: Painter? = null,
    onNavigationClick: (() -> Unit)? = null,
    onSaveClick: (() -> Unit)? = null,
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors()
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .clickable(
                            onClick = onNavigationClick,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                ) {
                    Icon(
                        painter = navigationIcon,
                        contentDescription = stringResource(R.string.notes_back),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.notes_back),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        },
        actions = {
            if (onSaveClick != null) {
                TextButton(
                    onClick = onSaveClick,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.notes_save),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag("TopAppBar").padding(horizontal = 12.dp),
        expandedHeight = expandedHeight
    )
}
