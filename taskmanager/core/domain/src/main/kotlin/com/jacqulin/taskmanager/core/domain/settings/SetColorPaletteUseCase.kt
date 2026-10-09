package com.jacqulin.taskmanager.core.domain.settings

import com.jacqulin.taskmanager.core.model.ColorPalette
import com.jacqulin.taskmanager.data.repository.UserDataRepository
import javax.inject.Inject

class SetColorPaletteUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
) {
    suspend operator fun invoke(palette: ColorPalette) =
        userDataRepository.setColorPalette(palette)
}
