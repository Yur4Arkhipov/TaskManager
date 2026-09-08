package com.jacqulin.taskmanager.feature.notes.presentation.noteeditor

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
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
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var cameraSessionUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onEvent(NoteEditorEvent.ImageSelected(uri))
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        val uri = cameraSessionUri
        if (uri != null) {
            if (success) {
                viewModel.onEvent(NoteEditorEvent.ImageSelected(uri))
            } else {
                viewModel.onEvent(NoteEditorEvent.CameraCancelled(uri))
            }
        }
        cameraSessionUri = null
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.onEvent(NoteEditorEvent.CameraPermissionGranted)
        } else {
            viewModel.onEvent(NoteEditorEvent.CameraPermissionDenied)
        }
    }

    val voicePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.onEvent(NoteEditorEvent.VoicePermissionGranted)
        } else {
            viewModel.onEvent(NoteEditorEvent.VoicePermissionDenied)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NoteEditorEffect.LaunchGallery -> {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
                is NoteEditorEffect.LaunchCamera -> {
                    cameraSessionUri = effect.uri
                    cameraLauncher.launch(effect.uri)
                }
                NoteEditorEffect.RequestCameraPermission -> {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                NoteEditorEffect.RequestVoicePermission -> {
                    voicePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                NoteEditorEffect.NavigateBack -> {
                    onBack()
                }
                is NoteEditorEffect.ShowError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val screenTitleRes = if (noteId == null) {
        R.string.notes_add_note
    } else {
        R.string.notes_edit_note
    }

    val imageModel = if (uiState.isImageRemoved) {
        null
    } else {
        uiState.selectedImageUri ?: uiState.imagePath
    }

    Scaffold(
        topBar = {
            CenterAlignedAppBar(
                titleRes = screenTitleRes,
                navigationIcon = painterResource(R.drawable.ic_arrow_back),
                onNavigationClick = {
                    viewModel.onEvent(NoteEditorEvent.BackClicked)
                },
                onSaveClick = {
                    viewModel.onEvent(NoteEditorEvent.SaveClicked)
                }
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
                onValueChange = { newTitle ->
                    viewModel.onEvent(NoteEditorEvent.TitleChanged(newTitle))
                },
                errorMessage = uiState.titleError
            )

            Spacer(Modifier.height(16.dp))

            NoteContentField(
                value = uiState.content,
                onValueChange = { newContent ->
                    viewModel.onEvent(NoteEditorEvent.ContentChanged(newContent))
                },
                onVoiceInputClick = {
//                    if (uiState.voiceRecordingState is VoiceState.Recording) {
//                        viewModel.stopVoiceInput()
//                    } else {
                    viewModel.onEvent(NoteEditorEvent.VoiceInputStartClicked)
//                    }
                },
                onStopVoice = {
                    viewModel.onEvent(NoteEditorEvent.VoiceInputStopClicked)
                },
                voiceRecordingState = uiState.voiceRecordingState
            )

            Spacer(Modifier.height(16.dp))

            if (imageModel != null) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = imageModel,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )

                    FilledIconButton(
                        onClick = {
                            viewModel.onEvent(NoteEditorEvent.ImageRemoved)
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cross),
                            contentDescription = null,
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
                            viewModel.onEvent(NoteEditorEvent.ImageAddFromGalleryClicked)
                        },
                        modifier = Modifier.weight(1f),
                    )

                    ImagePickerButton(
                        icon = painterResource(R.drawable.ic_camera),
                        text = stringResource(R.string.notes_add_from_camera),
                        onClick = {
                            viewModel.onEvent(NoteEditorEvent.ImageAddFromCameraClicked)
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
            noteId = 1,
            onBack = { }
        )
    }
}
