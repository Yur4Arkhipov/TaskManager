package com.jacqulin.taskmanager.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    navigationIcon: Painter? = null,
    onNavigationClick: (() -> Unit)? = null,
    saveEnabled: Boolean = false,
    onSaveClick: (() -> Unit)? = null,
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(titleRes)) },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .clickable(
                            onClick = onNavigationClick
                        )
                ) {
                    Icon(
                        painter = navigationIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text("Назад")
                }
            }
        },
        actions = {
            if (onSaveClick != null) {
                TextButton(
                    onClick = onSaveClick,
                    enabled = saveEnabled,
                ) {
                    Text("Сохранить")
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag("TopAppBar"),
        expandedHeight = expandedHeight
    )
}
