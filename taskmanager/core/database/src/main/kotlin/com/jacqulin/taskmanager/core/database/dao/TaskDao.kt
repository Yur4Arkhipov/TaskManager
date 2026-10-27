package com.jacqulin.taskmanager.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.jacqulin.taskmanager.core.database.entity.TagEntity
import com.jacqulin.taskmanager.core.database.entity.TaskEntity
import com.jacqulin.taskmanager.core.database.entity.TaskTagCrossRef
import com.jacqulin.taskmanager.core.database.entity.TaskWithTags
import com.jacqulin.taskmanager.core.database.entity.toEntity
import com.jacqulin.taskmanager.core.model.Task
import com.jacqulin.taskmanager.core.model.Tag
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun observeTasks(): Flow<List<TaskWithTags>>

    @Transaction
    suspend fun insertTask(task: Task) {
        val taskId = insertTaskEntity(task.toEntity()).toInt()
        replaceTaskTags(taskId, task.tags)
    }

    @Transaction
    suspend fun updateTask(task: Task) {
        updateTaskEntity(task.toEntity())
        replaceTaskTags(task.id, task.tags)
    }

    @Transaction
    suspend fun updateDayPlanOrder(taskIds: List<Int>) {
        taskIds.forEachIndexed { position, taskId ->
            updateTaskDayPlanOrder(taskId = taskId, position = position)
        }
    }

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("UPDATE tasks SET dayPlanOrder = :position WHERE id = :taskId")
    suspend fun updateTaskDayPlanOrder(taskId: Int, position: Int)

    // Help queries
    @Query("DELETE FROM task_tag_cross_ref WHERE taskId = :taskId")
    suspend fun deleteTaskTags(taskId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTaskTag(crossRef: TaskTagCrossRef)

    @Insert
    suspend fun insertTaskEntity(task: TaskEntity): Long

    @Update
    suspend fun updateTaskEntity(task: TaskEntity)

    @Query("SELECT id FROM tags WHERE title = :title")
    suspend fun getTagId(title: String): Int
}

private suspend fun TaskDao.replaceTaskTags(taskId: Int, tags: List<Tag>) {
    deleteTaskTags(taskId)
    tags.map { it.name }
        .forEach { name ->
            insertTag(TagEntity(title = name))
            insertTaskTag(TaskTagCrossRef(taskId = taskId, tagId = getTagId(name)))
        }
}
