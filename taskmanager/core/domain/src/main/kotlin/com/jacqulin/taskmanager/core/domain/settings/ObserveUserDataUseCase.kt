package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.model.UserData
import com.jacqulin.taskmanager.data.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserDataUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(): Flow<UserData> = userDataRepository.userData
}
