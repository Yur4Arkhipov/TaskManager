package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.database.dao.FocusSessionDao
import com.jacqulin.taskmanager.core.database.entity.asExternalModel
import com.jacqulin.taskmanager.core.database.entity.toEntity
import com.jacqulin.taskmanager.core.model.FocusSession
import kotlinx.coroutines.flow.Flow

class FocusSessionRepositoryImpl(
    private val dao: FocusSessionDao
): FocusSessionRepository {

    override suspend fun create(session: FocusSession): Long =
        dao.insert(session.toEntity())

    override suspend fun getById(sessionId: Long): FocusSession? =
        dao.getById(sessionId)?.asExternalModel()

    override fun observeById(sessionId: Long): Flow<FocusSession?> {
        TODO()
    }

    override suspend fun update(session: FocusSession) {
        TODO()
    }
}
