package com.jacqulin.taskmanager.core.data.repository

import com.jacqulin.taskmanager.core.database.dao.TagsDao
import com.jacqulin.taskmanager.core.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TagsRepositoryImpl @Inject constructor(
    private val dao: TagsDao
) : TagsRepository {

    override fun observeTags(): Flow<List<Tag>> = dao.observeTags().map { tags ->
        tags.map {
            Tag(
                id = it.id,
                name = it.title,
                color = it.color
            )
        }
    }

    override suspend fun getTags(): List<Tag> = dao.getTags().map { tagEntity ->
        Tag(
            id = tagEntity.id,
            name = tagEntity.title,
            color = tagEntity.color
        )
    }

    override suspend fun createTag(title: String) = dao.createTag(title)

    override suspend fun renameTag(tagId: Int, title: String) = dao.renameTag(tagId, title)

    override suspend fun deleteTag(tagId: Int) = dao.deleteTag(tagId)
}
