package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.usecases.settings.ReadInstructionsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.settings.SaveInstructionsUseCase

class OnboardingUseCases (
    val saveInstructionsUseCase: SaveInstructionsUseCase,
    val readInstructionsUseCase: ReadInstructionsUseCase
)