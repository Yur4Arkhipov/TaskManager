package com.jacqulin.taskmanager.feature.settings.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.voice.data.model.SpeechProviderCatalog
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.feature.settings.presentation.SpeechFieldUiState

@Composable
fun VoiceInputSettingsSection(
    selectedProvider: String?,
    fields: List<SpeechFieldUiState>,
    canSave: Boolean,
    isSaving: Boolean,
    onProviderSelected: (String) -> Unit,
    onCredentialChanged: (key: String, value: String) -> Unit,
    onSave: () -> Unit,
) {
    val selected = selectedProvider?.let(SpeechProviderCatalog::byId)

    ExpandableSettingsSectionCard(
        title = selected?.name ?: "Голосовой ввод",
        subtitle = if (fields.any { it.isSaved }) "Настроено" else "Добавить API-токен",
        initiallyExpanded = false,
    ) {
        Text(
            text = "Выберите модель для обработки голоса",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SpeechProviderCatalog.providers.forEach { provider ->
            ProviderOptionCard(
                title = provider.name,
                subtitle = provider.description,
                selected = provider.id == selectedProvider,
                onClick = { onProviderSelected(provider.id) },
            )
        }

        Spacer(Modifier.height(8.dp))

        fields.forEach { fieldState ->
            CredentialField(
                state = fieldState,
                enabled = !isSaving,
                onValueChange = { onCredentialChanged(fieldState.field.key, it) },
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = "Данные хранятся только на этом устройстве",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onSave,
            enabled = canSave,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(if (isSaving) "Сохранение…" else "Сохранить")
        }
    }
}

@Composable
private fun CredentialField(
    state: SpeechFieldUiState,
    enabled: Boolean,
    onValueChange: (String) -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    Column {
        Text(state.field.label, style = MaterialTheme.typography.titleSmall)

        OutlinedTextField(
            value = state.value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (!state.field.isSecret || visible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation(mask = '*')
            },
            trailingIcon = {
                if (state.field.isSecret) {
                    IconButton(onClick = { visible = !visible }) {
                        Icon(
                            painter = if (visible) painterResource(R.drawable.ic_eye_visibility)
                            else painterResource(R.drawable.ic_eye_visibility_off),
                            contentDescription = if (visible) "Скрыть" else "Показать",
                        )
                    }
                }
            },
            placeholder = {
                val text = when {
                    state.isSaved && state.value.isEmpty() -> "..."
                    else -> state.field.placeholder
                }
                Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant)
            },
        )
    }
}