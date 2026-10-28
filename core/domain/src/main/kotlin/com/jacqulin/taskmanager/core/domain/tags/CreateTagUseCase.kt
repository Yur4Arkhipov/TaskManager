package com.jacqulin.taskmanager.core.domain.tags

import com.jacqulin.taskmanager.core.data.repository.TagsRepository
import javax.inject.Inject

class CreateTagUseCase @Inject constructor(
    private val repository: TagsRepository,
) {
    suspend operator fun invoke(title: String) = repository.createTag(title)
}
