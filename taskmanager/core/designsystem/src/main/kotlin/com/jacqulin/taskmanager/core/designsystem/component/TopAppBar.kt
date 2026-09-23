package com.jacqulin.taskmanager.core.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    navigationIcon: Painter? = null,
    onNavigationClick: (() -> Unit)? = null,
    actionIcon: Painter? = null,
    onActionClick: (() -> Unit)? = null,
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors()
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (navigationIcon != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        painter =  navigationIcon,
                        contentDescription = null
                    )
                }
            }
        },
        actions = {
            if (actionIcon != null && onActionClick != null) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        painter = actionIcon,
                        contentDescription = null,
                    )
                }
            }
        },
        colors = colors,
        modifier = modifier.testTag("TopAppBar"),
        expandedHeight = expandedHeight
    )
}
