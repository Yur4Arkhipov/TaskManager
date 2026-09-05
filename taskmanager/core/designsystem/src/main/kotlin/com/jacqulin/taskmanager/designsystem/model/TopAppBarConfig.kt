package com.jacqulin.taskmanager.designsystem.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.painter.Painter

data class TopAppBarConfig(
    @StringRes val titleRes: Int,
    val navigationIcon: Painter? = null,
    val onNavigationClick: (() -> Unit)? = null,
    val actionIcon: Painter? = null,
    val onActionClick: (() -> Unit)? = null,
)
