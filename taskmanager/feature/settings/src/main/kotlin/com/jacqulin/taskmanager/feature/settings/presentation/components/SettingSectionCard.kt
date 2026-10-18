package com.jacqulin.taskmanager.feature.settings.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun SettingsSectionCard(
    title: String,
    content: @Composable () -> Unit,
    isExpanded: Boolean = true,
    onToggleExpanded: (() -> Unit)? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (onToggleExpanded != null) {
                            Modifier.clickable { onToggleExpanded() }
                        } else {
                            Modifier
                        }
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (onToggleExpanded != null) {
                    Icon(
                        painter = if (isExpanded) painterResource(R.drawable.ic_arrow_up)
                            else painterResource(R.drawable.ic_arrow_down),
                        contentDescription = if (isExpanded) stringResource(R.string.settings_expandable_collapse)
                            else stringResource(R.string.settings_expandable_expand),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            if (isExpanded) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
                ) {
                    content()
                }
            }
        }
    }
}
