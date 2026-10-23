package com.jacqulin.taskmanager.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.jacqulin.taskmanager.core.database.entity.TagEntityNew
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Query("SELECT * FROM tagsNew ORDER BY id DESC")
    fun observeTags(): Flow<List<TagEntityNew>>

    @Query("INSERT OR IGNORE INTO tagsNew (title) VALUES (:title)")
    suspend fun createTag(title: String)

    @Query("UPDATE tagsNew SET title = :title WHERE id = :tagId")
    suspend fun renameTag(tagId: Int, title: String)

    @Query("DELETE FROM tagsNew WHERE id = :tagId")
    suspend fun deleteTag(tagId: Int)
}
