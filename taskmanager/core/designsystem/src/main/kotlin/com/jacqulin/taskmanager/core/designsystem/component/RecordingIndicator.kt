package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RecordingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(
        label = "recording"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(12) { index ->

            val height by infiniteTransition.animateFloat(
                initialValue = 4f,
                targetValue = 18f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 500,
                        delayMillis = index * 40,
                        easing = FastOutSlowInEasing,
                    ),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "bar_$index",
            )

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(height.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}
