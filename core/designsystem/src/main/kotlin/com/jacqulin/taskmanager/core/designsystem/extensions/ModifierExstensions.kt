package com.jacqulin.taskmanager.core.designsystem.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    cornerRadius: Dp,
    dashLength: Float = 10f,
    gapLength: Float = 6f,
) = drawBehind {
    val strokeWidth = width.toPx()
    val radius = cornerRadius.toPx()

    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    strokeWidth / 2,
                    strokeWidth / 2,
                    size.width - strokeWidth / 2,
                    size.height - strokeWidth / 2,
                ),
                cornerRadius = CornerRadius(radius),
            ),
        )
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength, gapLength),
            ),
        ),
    )
}
