package com.jacqulin.taskmanager.designsystem.model

import androidx.compose.ui.graphics.painter.Painter

data class BottomBarItem(
    val icon: Painter,
    val contentDescription: String,
    val selected: Boolean,
    val onClick: () -> Unit
)
