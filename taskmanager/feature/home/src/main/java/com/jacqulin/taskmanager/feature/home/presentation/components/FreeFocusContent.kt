package com.jacqulin.taskmanager.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun FreeFocusContent() {
    Column {
        Text(
            text = "Без обратного отсчёта. Завершите сессию, когда почувствуете, что пора.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        TimerCard()
    }
}

@Composable
private fun TimerCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_infinity),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(.7f),
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = "Секундомер считает время вперед. Пауза будет доступна в любой момент.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun FreeFocusContentPreview() {
    TaskManagerTheme {
        FreeFocusContent()
    }
}
