package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import com.jacqulin.taskmanager.designsystem.R
import com.jacqulin.taskmanager.designsystem.component.CenterAlignedAppBar
import com.jacqulin.taskmanager.designsystem.theme.TaskManagerTheme
import com.jacqulin.taskmanager.feature.notes.navigation.NoteEditorRoute
import com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.components.ImagePickerButton
import com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.components.NoteContentField
import com.jacqulin.taskmanager.feature.notes.presentation.noteeditor.components.NoteTitleField

@NavDestination(route = NoteEditorRoute::class)
@Composable
fun NoteEditorScreen(
    noteId: Int? = null,
    onBack: () -> Unit,
    onAddImage: () -> Unit = {},
    onRemoveImage: () -> Unit = {},
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val screenTitleRes = if (noteId == null) {
        R.string.notes_add_note
    } else {
        R.string.notes_edit_note
    }

    Scaffold(
        topBar = {
            CenterAlignedAppBar(
                titleRes = screenTitleRes,
                navigationIcon = painterResource(R.drawable.ic_arrow_back),
                onNavigationClick = onBack,
                onSaveClick = { }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                )
        ) {
            NoteTitleField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChanged
            )

            Spacer(Modifier.height(16.dp))

            NoteContentField(
                value = uiState.content,
                onValueChange = viewModel::onContentChanged,
                onVoiceInputClick = {
                },
            )

            Spacer(Modifier.height(16.dp))

            if (uiState.hasImage) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.ic_image),
                        contentDescription = null
                    )

                    FilledIconButton(
                        onClick = {
                            viewModel.onImageRemoved()
                            onRemoveImage()
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cross),
                            contentDescription = null
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ImagePickerButton(
                        icon = painterResource(R.drawable.ic_image),
                        text = stringResource(R.string.notes_add_from_files),
                        onClick = {
                            viewModel.onImageAdded()
                            onAddImage()
                        },
                        modifier = Modifier.weight(1f),
                    )

                    ImagePickerButton(
                        icon = painterResource(R.drawable.ic_camera),
                        text = stringResource(R.string.notes_add_from_camera),
                        onClick = {
                            viewModel.onImageAdded()
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
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
onBack = { }
        )
    }
}
