package com.jacqulin.taskmanager.feature.home.presentation.focus

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.home.presentation.components.FreeFocusContent
import com.jacqulin.taskmanager.feature.home.presentation.components.PomodoroFocusContent
import com.jacqulin.taskmanager.feature.home.presentation.components.TimerFocusContent
import com.jacqulin.taskmanager.feature.home.presentation.model.FocusMode
import com.jacqulin.taskmanager.feature.home.presentation.state.FocusConfigUiState

@Composable
fun FocusConfigScreen(
    onBackClick: () -> Unit,
    onNavigateToFocusSession: (FocusMode) -> Unit,
    viewModel: FocusViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FocusConfigContent(
        state = state,
        onBackClick = onBackClick,
        onNavigateToFocusSession = onNavigateToFocusSession,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun FocusConfigContent(
    state: FocusConfigUiState,
    onBackClick: () -> Unit,
    onNavigateToFocusSession: (FocusMode) -> Unit = {},
    onEvent: (FocusConfigEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        BackRow(onBackClick)

        Spacer(Modifier.height(20.dp))

        SelectedTaskCard(taskTitle = state.taskTitle)

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Как будем работать?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(10.dp))

        SegmentedButton(
            selectedMode = state.focusMode,
            onModeSelected = { mode ->
                onEvent(FocusConfigEvent.ModeChanged(mode))
            }
        )

        Spacer(Modifier.height(8.dp))

        when (state.focusMode) {
            FocusMode.POMODORO -> PomodoroFocusContent(state)
            FocusMode.TIMER -> TimerFocusContent(
                selectedDuration = state.timerDurationMinutes,
                onDurationSelected = { minutes ->
                    onEvent(FocusConfigEvent.TimerDurationChanged(minutes))
                },
                onCustomTimerDurationConfigured = {

                }
            )
            FocusMode.FREE -> FreeFocusContent()
        }

        Spacer(Modifier.weight(1f))

        val text = when (state.focusMode) {
            FocusMode.POMODORO -> "Только то, что важно сейчас"
            FocusMode.TIMER -> "${state.timerDurationMinutes} минут для эффективной работы"
            FocusMode.FREE -> "Спокойное выполнение задачи"
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary.copy(.6f),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        StartFocusButton(
            mode = state.focusMode,
            onClick = onNavigateToFocusSession,
        )
    }
}

@Composable
private fun SelectedTaskCard(
    taskTitle: String,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_assignment),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Выбранная задача",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (taskTitle.isNotBlank()) {
                Text(
                    text = taskTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun SegmentedButton(
    selectedMode: FocusMode,
    onModeSelected: (FocusMode) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            listOf(
                FocusMode.POMODORO,
                FocusMode.TIMER,
                FocusMode.FREE
            ).forEach { mode ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (selectedMode == mode) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .clickable { onModeSelected(mode) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selectedMode == mode) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selectedMode == mode) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StartFocusButton(
    mode: FocusMode,
    onClick: (FocusMode) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.primary,
        onClick = { onClick(mode) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_play),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Начать фокус",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
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
fun FocusConfigContentPreview_Pomodoro() {
    TaskManagerTheme {
        FocusConfigContent(
            state = FocusConfigUiState(
                taskTitle = "Задача 1 Задача 1 Задача 1 Задача 1 Задача 1 Задача 1 Pflfx",
                focusMode = FocusMode.POMODORO
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun FocusConfigContentPreview_Timer() {
    TaskManagerTheme {
        FocusConfigContent(
            state = FocusConfigUiState(
                taskTitle = "Задача 1",
                focusMode = FocusMode.TIMER
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun FocusConfigContentPreview_Free() {
    TaskManagerTheme {
        FocusConfigContent(
            state = FocusConfigUiState(
                taskTitle = "Задача 1",
                focusMode = FocusMode.FREE
            ),
            onBackClick = {},
            onEvent = {}
        )
    }
}
