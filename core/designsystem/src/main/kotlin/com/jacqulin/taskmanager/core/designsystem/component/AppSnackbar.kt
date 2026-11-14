package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.model.AppSnackbarType

data class AppSnackbarVisuals(
    override val message: String,
    val type: AppSnackbarType,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals

@Composable
fun AppSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier
) {
    val visuals = data.visuals as? AppSnackbarVisuals
    val colorScheme = MaterialTheme.colorScheme

    val containerColor = when (visuals?.type) {
        AppSnackbarType.SUCCESS -> colorScheme.primaryContainer.copy(alpha = 0.8f)
        AppSnackbarType.ERROR -> colorScheme.errorContainer.copy(alpha = 0.8f)
        AppSnackbarType.INFO, null -> colorScheme.surfaceVariant.copy(alpha = 0.8f)
    }

    val contentColor = when (visuals?.type) {
        AppSnackbarType.SUCCESS -> colorScheme.onPrimaryContainer
        AppSnackbarType.ERROR -> colorScheme.onErrorContainer
        AppSnackbarType.INFO, null -> colorScheme.onSurfaceVariant
    }

    val shape = RoundedCornerShape(20.dp)

    Snackbar(
        modifier = modifier.padding(horizontal = 16.dp),
        snackbarData = data,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionColor = colorScheme.primary,
        dismissActionContentColor = contentColor,
        actionOnNewLine = false
    )
}
