package com.jacqulin.taskmanager.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jacqulin.taskmanager.core.database.dao.NoteDao
import com.jacqulin.taskmanager.core.database.entity.NoteEntity

@Database(
    entities = [
        NoteEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class TaskManagerDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}
