package com.jacqulin.taskmanager.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jacqulin.taskmanager.core.database.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {

    @Insert
    suspend fun insert(session: FocusSessionEntity): Long

    @Query("SELECT * FROM focus_sessions WHERE id = :sessionId")
    suspend fun getById(sessionId: Long): FocusSessionEntity?

    @Query("SELECT * FROM focus_sessions WHERE id = :sessionId")
    fun observeById(sessionId: Long): Flow<FocusSessionEntity?>
}


