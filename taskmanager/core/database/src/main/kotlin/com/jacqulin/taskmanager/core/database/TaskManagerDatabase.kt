package com.jacqulin.taskmanager.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jacqulin.taskmanager.core.database.dao.NoteDao
import com.jacqulin.taskmanager.core.database.dao.TagsDao
import com.jacqulin.taskmanager.core.database.dao.TaskDao
import com.jacqulin.taskmanager.core.database.entity.NoteEntity
import com.jacqulin.taskmanager.core.database.entity.TagEntity
import com.jacqulin.taskmanager.core.database.entity.TagEntityNew
import com.jacqulin.taskmanager.core.database.entity.TaskTagCrossRef
import com.jacqulin.taskmanager.core.database.entity.TaskEntity

@Database(
    entities = [
        NoteEntity::class,
        TaskEntity::class,
        TagEntity::class,
        TagEntityNew::class,
        TaskTagCrossRef::class,
    ],
    version = 4,
    exportSchema = true,
)
abstract class TaskManagerDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun tagsDao(): TagsDao
}
