package com.jacqulin.taskmanager.feature.notes.domain.usecase

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetNotesUseCase @Inject constructor() {
    private val notes = listOf(
        Note(
            id = 1,
            title = "Сделать план на неделю",
            content = "Нужно распределить задачи на неделю, определить приоритеты и подготовить список дел.",
            createdAtMillis = 1_756_985_600_000L,
            hasPreviewImage = false
        ),
        Note(
            id = 2,
            title = "Подготовить заметки к встрече",
            content = "Собрать тезисы, подготовить вопросы и проверить материалы по проекту.",
            createdAtMillis = 1_756_899_200_000L,
            hasPreviewImage = true
        ),
        Note(
            id = 3,
            title = "Идеи для новых задач",
            content = "Добавить автоматические уведомления, улучшить фильтрацию заметок и доработать дизайн.",
            createdAtMillis = 1_756_840_000_000L,
            hasPreviewImage = false
        )
    )

    operator fun invoke(): Flow<List<Note>> = flowOf(notes)

    fun getById(noteId: Int): Flow<Note?> = flowOf(notes.firstOrNull { it.id == noteId })
}
