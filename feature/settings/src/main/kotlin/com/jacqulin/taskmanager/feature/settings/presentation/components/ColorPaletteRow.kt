package com.jacqulin.taskmanager.feature.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.AmberSchemeColor
import com.jacqulin.taskmanager.core.designsystem.extensions.fadeScrollableHorizontalEdges
import com.jacqulin.taskmanager.core.designsystem.theme.BlueSchemeColor
import com.jacqulin.taskmanager.core.designsystem.theme.CyanSchemeColor
import com.jacqulin.taskmanager.core.designsystem.theme.GreenSchemeColor
import com.jacqulin.taskmanager.core.designsystem.theme.MalachiteSchemeColor
import com.jacqulin.taskmanager.core.designsystem.theme.OrangeSchemeColor
import com.jacqulin.taskmanager.core.designsystem.theme.PinkSchemeColor
import com.jacqulin.taskmanager.core.designsystem.theme.PurpleSchemeColor
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun ColorPaletteRow(
    selectedPalette: ColorPalette,
    onPaletteSelected: (ColorPalette) -> Unit
) {
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        modifier = Modifier.fadeScrollableHorizontalEdges(
            listState = listState,
            layoutDirection = LocalLayoutDirection.current,
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(ColorPalette.entries.toList()) { palette ->
            val isSelected = palette == selectedPalette

            val borderColor = if (isSelected) {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            } else {
                Color.Transparent
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                    ) { onPaletteSelected(palette) }
                    .then(
                        if (isSelected) {
                            Modifier.border(1.dp, borderColor, CircleShape)
                        } else {
                            Modifier
                        }
                    )
                    .background(
                        color = when (palette) {
                            ColorPalette.GREEN -> GreenSchemeColor
                            ColorPalette.CYAN -> CyanSchemeColor
                            ColorPalette.ORANGE -> OrangeSchemeColor
                            ColorPalette.PURPLE -> PurpleSchemeColor
                            ColorPalette.PINK -> PinkSchemeColor
                            ColorPalette.MALACHITE -> MalachiteSchemeColor
                            ColorPalette.AMBER -> AmberSchemeColor
                            ColorPalette.BLUE -> BlueSchemeColor
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = stringResource(R.string.settings_color_selected),
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
