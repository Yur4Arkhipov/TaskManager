package com.jacqulin.taskmanager.feature.notes.data.repository

import com.jacqulin.taskmanager.feature.notes.domain.model.Note
import com.jacqulin.taskmanager.feature.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepositoryImpl @Inject constructor() : NotesRepository {

    // initial sample data (kept from previous hardcoded usecase)
    private val initial = listOf(
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

    private val _notes = MutableStateFlow(initial)
    private val mutex = Mutex()
    private val idGenerator = AtomicInteger((_notes.value.maxOfOrNull { it.id } ?: 0) + 1)

    override fun getAllNotes(): Flow<List<Note>> = _notes

    override fun getNoteById(id: Int): Flow<Note?> = _notes.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun addNote(note: Note): Int {
        // assign new id and prepend with createdAt if necessary
        val newId = idGenerator.getAndIncrement()
        val newNote = note.copy(id = newId)
        mutex.withLock {
            _notes.value = _notes.value + newNote
        }
        return newId
    }

    override suspend fun updateNote(note: Note) {
        mutex.withLock {
            _notes.value = _notes.value.map { if (it.id == note.id) note else it }
        }
    }

    override suspend fun deleteNote(id: Int) {
        mutex.withLock {
            _notes.value = _notes.value.filterNot { it.id == id }
        }
    }
}
