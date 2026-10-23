package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagsRepository {
    fun observeTags(): Flow<List<Tag>>
    suspend fun getTags(): List<Tag>
    suspend fun createTag(title: String)
    suspend fun renameTag(tagId: Int, title: String)
    suspend fun deleteTag(tagId: Int)
}
