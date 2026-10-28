package com.jacqulin.taskmanager.core.designsystem.extensions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.fadeScrollableVerticalEdges(
    listState: LazyListState,
    fadeLength: Dp = 24.dp,
): Modifier =
    graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()

            val fadePx = fadeLength.toPx().coerceAtMost(size.height * 0.45f)
            val fadeTop = listState.canScrollBackward
            val fadeBottom = listState.canScrollForward
            if (size.height > 0f && fadePx > 0f && (fadeTop || fadeBottom)) {
                val fadeFraction = fadePx / size.height
                val colorStops = buildList {
                    add(0f to if (fadeTop) Color.Transparent else Color.Black)
                    if (fadeTop) add(fadeFraction to Color.Black)
                    if (fadeBottom) add((1f - fadeFraction) to Color.Black)
                    add(1f to if (fadeBottom) Color.Transparent else Color.Black)
                }.toTypedArray()

                drawRect(
                    brush = Brush.verticalGradient(
                        colorStops = colorStops,
                        startY = 0f,
                        endY = size.height,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }
        }

fun Modifier.fadeScrollableHorizontalEdges(
    listState: LazyListState,
    layoutDirection: LayoutDirection,
    fadeLength: Dp = 24.dp,
): Modifier =
    graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            drawContent()

            val fadePx = fadeLength.toPx().coerceAtMost(size.width * 0.45f)
            val isRtl = layoutDirection == LayoutDirection.Rtl
            val fadeLeft = if (isRtl) listState.canScrollForward else listState.canScrollBackward
            val fadeRight = if (isRtl) listState.canScrollBackward else listState.canScrollForward
            if (size.width > 0f && fadePx > 0f && (fadeLeft || fadeRight)) {
                val fadeFraction = fadePx / size.width
                val colorStops = buildList {
                    add(0f to if (fadeLeft) Color.Transparent else Color.Black)
                    if (fadeLeft) add(fadeFraction to Color.Black)
                    if (fadeRight) add((1f - fadeFraction) to Color.Black)
                    add(1f to if (fadeRight) Color.Transparent else Color.Black)
                }.toTypedArray()

                drawRect(
                    brush = Brush.horizontalGradient(
                        colorStops = colorStops,
                        startX = 0f,
                        endX = size.width,
                    ),
                    blendMode = BlendMode.DstIn,
                )
            }
        }
