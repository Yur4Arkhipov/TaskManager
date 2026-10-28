package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.model.FocusSession
import kotlinx.coroutines.flow.Flow

interface FocusSessionRepository {

    suspend fun create(session: FocusSession): Long

    suspend fun getById(sessionId: Long): FocusSession?

    fun observeById(sessionId: Long): Flow<FocusSession?>

    suspend fun update(session: FocusSession)
}
