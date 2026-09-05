package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.notes.navigation.NoteEditorRoute
import com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.components.ImagePickerButton
import com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.components.NoteContentField
import com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.components.NoteTitleField

@NavDestination(route = NoteEditorRoute::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
//    noteId: String?,
//    onBack: () -> Unit = {},
//    onSave: () -> Unit = {},
    onAddImage: () -> Unit = {},
//    onRemoveImage: () -> Unit = {},
//    onVoiceInput: () -> Unit = {},
    initialTitle: String = "",
    initialContent: String = "",
//    initialImageBytes: ByteArray? = null,
) {
    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NoteTitleField(
                value = title,
                onValueChange = { title = it },
            )

            Spacer(Modifier.height(16.dp))

            NoteContentField(
                value = content,
                onValueChange = { content = it },
                onVoiceInputClick = {
                },
            )

            Spacer(Modifier.height(16.dp))
//
//            // Image preview section
//            if (imageBytes != null) {
//                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = CardDefaults.cardColors(
//                            containerColor = Color(0xFFF5F5F5)
//                        )
//                    ) {
//                        Box {
//                            /*Image(
//                                bitmap = imageBytes!!.asImageBitmap(),
//                                contentDescription = "Изображение заметки",
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(200.dp)
//                            )*/
//                            IconButton(
//                                onClick = onRemoveImage,
//                                modifier = Modifier
//                                    .align(Alignment.TopEnd)
//                                    .padding(8.dp)
//                                    .size(32.dp)
//                            ) {
//                     /*           Icon(
//                                    imageVector = Icons.Default.Delete,
//                                    contentDescription = "Удалить изображение",
//                                    tint = Color.Red
//                                )*/
//                            }
//                        }
//                    }
//                }
//            }
//
            // Image picker section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ImagePickerButton(
                    icon = painterResource(R.drawable.ic_image),
                    text = stringResource(R.string.notes_add_from_files),
                    onClick = onAddImage,
                    modifier = Modifier.weight(1f),
                )

                ImagePickerButton(
                    icon = painterResource(R.drawable.ic_camera),
                    text = stringResource(R.string.notes_add_from_camera),
                    onClick = {
                        // TODO
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@NavPreview(route = NoteEditorRoute::class, primary = true)
@Preview
@Composable
fun NoteEditorScreenPreview() {
    TaskManagerTheme {
        NoteEditorScreen(
//            noteId = null
        )
    }
}

@Preview
@Composable
fun NoteEditorScreenWithImagePreview() {
    TaskManagerTheme {
//        val sampleBytes = ByteArrayOutputStream().toByteArray()
        NoteEditorScreen(
//            noteId = "123",
            initialTitle = "Заголовок",
            initialContent = "Текст заметки",
//            initialImageBytes = sampleBytes
        )
    }
}
