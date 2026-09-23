package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacqulin.taskmanager.designsystem.R

@Composable
fun NoteContentField(
    value: String,
    onValueChange: (String) -> Unit,
    onVoiceInputClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(
                width = 0.2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(12.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp),
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Unspecified,
                imeAction = ImeAction.Unspecified,
                platformImeOptions = null,
                showKeyboardOnFocus = null,
                hintLocales = null
            ),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = stringResource(R.string.notes_content_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                innerTextField()
            },
        )
        IconButton(
            onClick = onVoiceInputClick,
            modifier = Modifier.align(Alignment.BottomEnd),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_microphone),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
