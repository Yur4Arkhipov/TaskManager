package com.jacqulin.taskmanager.feature.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.theme.blueSchemeColor
import com.jacqulin.taskmanager.designsystem.theme.defaultSchemeColor
import com.jacqulin.taskmanager.designsystem.theme.greenSchemeColor
import com.jacqulin.taskmanager.designsystem.theme.orangeSchemeColor
import com.jacqulin.taskmanager.designsystem.theme.purpleSchemeColor

@Composable
fun ColorPaletteRow(
    selectedPalette: ColorPalette,
    onPaletteSelected: (ColorPalette) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                    .clickable { onPaletteSelected(palette) }
                    .then(
                        if (isSelected) {
                            Modifier.border(1.dp, borderColor, CircleShape)
                        } else {
                            Modifier
                        }
                    )
                    .clip(CircleShape)
                    .background(
                        color = when (palette) {
                            ColorPalette.DEFAULT -> defaultSchemeColor
                            ColorPalette.BLUE -> blueSchemeColor
                            ColorPalette.GREEN -> greenSchemeColor
                            ColorPalette.ORANGE -> orangeSchemeColor
                            ColorPalette.PURPLE -> purpleSchemeColor
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
