package com.carlostorres.wordsgame.game.domain.usecases

class OnboardingUseCases (
    val saveInstructionsUseCase: SaveInstructionsUseCase,
    val readInstructionsUseCase: ReadInstructionsUseCase
)