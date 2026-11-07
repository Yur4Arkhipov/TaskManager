package com.jacqulin.taskmanager.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun CreationDateLabel(createdAtMillis: Long) {
    val locale = LocalConfiguration.current.locales[0]

    val text = remember(createdAtMillis, locale) {
        Instant.ofEpochMilli(createdAtMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(
                DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.LONG)
                    .withLocale(locale)
            )
    }

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
    )
}
