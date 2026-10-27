package com.jacqulin.taskmanager.feature.home.presentation.focus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.state.FocusConfigUiState

@Composable
fun FocusConfigScreen(
    onBackClick: () -> Unit,
    viewModel: FocusViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is FocusConfigEffect.StartFocus -> {
//                    onStartFocus(
//                        effect.taskId,
//                        effect.durationMinutes,
//                        effect.mode,
//                    )
                }
            }
        }
    }

    FocusConfigContent(
        state = state,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun FocusConfigContent(
    state: FocusConfigUiState,
    onBackClick: () -> Unit,
    onEvent: (FocusConfigEvent) -> Unit
) {
    Column(
        modifier = Modifier.padding(12.dp)
    ) {
        BackRow(onBackClick)
        Spacer(Modifier.height(10.dp))

        SelectedTaskCard(
            state = state
        )
        Spacer(Modifier.height(12.dp))

    }
}

@Composable
private fun SelectedTaskCard(
    state: FocusConfigUiState
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 12.dp
            ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_assignment),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(.6f)
                )
                Text(
                    text = "Выбранная задача",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = state.taskTitle,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = .85f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BackRow(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onBackClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_back),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "Назад",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

    }
}

@Composable
@Preview(showSystemUi = true)
fun FocusConfigContentPreview() {
    TaskManagerTheme {
        FocusConfigContent(
            state = FocusConfigUiState(
                taskTitle = "Задача 1 Задача 1 Задача 1 Задача 1 Задача 1 Задача 1"
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}
