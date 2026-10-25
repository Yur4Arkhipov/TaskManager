package com.jacqulin.taskmanager.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.jacqulin.taskmanager.core.database.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Query("SELECT * FROM tags ORDER BY id DESC")
    fun observeTags(): Flow<List<TagEntity>>
    
    @Query("SELECT * FROM tags ORDER BY id DESC")
    suspend fun getTags(): List<TagEntity>

    @Query("INSERT OR IGNORE INTO tags (title) VALUES (:title)")
    suspend fun createTag(title: String)

    @Query("UPDATE tags SET title = :title WHERE id = :tagId")
    suspend fun renameTag(tagId: Int, title: String)

    @Query("DELETE FROM tags WHERE id = :tagId")
    suspend fun deleteTag(tagId: Int)
}
