package com.jacqulin.taskmanager.core.designsystem.model

import androidx.compose.ui.graphics.painter.Painter

data class BottomBarItem(
    val icon: Painter,
    val contentDescription: String,
    val enabled: Boolean,
    val selected: Boolean,
    val onClick: () -> Unit
)
