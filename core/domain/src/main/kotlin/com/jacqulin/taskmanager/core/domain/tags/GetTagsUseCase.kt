package com.jacqulin.taskmanager.core.domain.tags

import com.jacqulin.taskmanager.core.data.repository.TagsRepository
import javax.inject.Inject

class GetTagsUseCase @Inject constructor(
    private val repository: TagsRepository
) {
    suspend operator fun invoke() = repository.getTags()
}
