package com.jacqulin.taskmanager.core.domain.tags

import com.jacqulin.taskmanager.core.data.repository.TagsRepository
import javax.inject.Inject

class RenameTagUseCase @Inject constructor(
    private val repository: TagsRepository,
) {
    suspend operator fun invoke(tagId: Int, title: String) = repository.renameTag(tagId, title)
}
