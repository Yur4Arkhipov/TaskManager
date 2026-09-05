package com.jacqulin.taskmanager.feature.notes.domain.usecase

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetNotesUseCase @Inject constructor() {
    operator fun invoke(): Flow<List<Note>> = flowOf(
        listOf(
            Note(
                id = "1",
                title = "Сделать план на неделю",
                createdAtMillis = 1_756_985_600_000L,
                hasPreviewImage = false
            ),
            Note(
                id = "2",
                title = "Подготовить заметки к встрече",
                createdAtMillis = 1_756_899_200_000L,
                hasPreviewImage = true
            ),
            Note(
                id = "3",
                title = "Идеи для новых задач",
                createdAtMillis = 1_756_840_000_000L,
                hasPreviewImage = false
            )
        )
    )
}
