package com.jacqulin.taskmanager.feature.settings.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jacqulin.taskmanager.core.model.VoiceInputMode
import com.jacqulin.taskmanager.designsystem.R

@Composable
internal fun ProviderConfigFields(
    voiceInputMode: VoiceInputMode,
    onYandexTokensChanged: (String, String) -> Unit,
    onFutureTokenChanged: (String) -> Unit
) {
    when (voiceInputMode) {
        VoiceInputMode.LIVE -> Unit
        VoiceInputMode.YANDEX_SPEECH -> {
            var iamToken by remember { mutableStateOf("") }
            var folderId by remember { mutableStateOf("") }

            val hasChanged = iamToken != "" && folderId != ""

            Spacer(modifier = Modifier.height(6.dp))

            TokenTextField(
                value = iamToken,
                onValueChange = { iamToken = it },
                placeholder = R.string.yandex_iam_token_label,
            )

            Spacer(modifier = Modifier.height(6.dp))

            TokenTextField(
                value = folderId,
                onValueChange = { folderId = it },
                placeholder = R.string.yandex_folder_id_label,
            )

            AnimatedVisibility(visible = hasChanged) {
                Column {
                    Spacer(Modifier.height(6.dp))
                    Button(
                        onClick = {
                            onYandexTokensChanged(iamToken, folderId)
                            iamToken = ""
                            folderId = ""
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
        VoiceInputMode.FUTURE -> {
            var token by remember { mutableStateOf("") }
            val hasChanged = token != ""

            Spacer(modifier = Modifier.height(6.dp))

            TokenTextField(
                value = token,
                onValueChange = { token = it },
                placeholder = R.string.yandex_iam_token_label,
            )

            AnimatedVisibility(visible = hasChanged) {
                Column {
                    Spacer(Modifier.height(6.dp))
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}
