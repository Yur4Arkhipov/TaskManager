package com.jacqulin.taskmanager.core.domain.tags

import com.jacqulin.taskmanager.core.data.repository.TagsRepository
import com.jacqulin.taskmanager.core.model.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageTagsUseCases @Inject constructor(
    private val repository: TagsRepository,
) {
    fun observeTags(): Flow<List<Tag>> = repository.observeTags()
    suspend fun create(title: String) = repository.createTag(title)
    suspend fun rename(tagId: Int, title: String) = repository.renameTag(tagId, title)
    suspend fun delete(tagId: Int) = repository.deleteTag(tagId)
}
