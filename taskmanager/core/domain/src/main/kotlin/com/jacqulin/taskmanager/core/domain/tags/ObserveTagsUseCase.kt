package com.jacqulin.taskmanager.core.domain.tags

import com.jacqulin.taskmanager.core.data.repository.TagsRepository
import com.jacqulin.taskmanager.core.model.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTagsUseCase @Inject constructor(
    private val repository: TagsRepository,
) {
    operator fun invoke(): Flow<List<Tag>> = repository.observeTags()
}
