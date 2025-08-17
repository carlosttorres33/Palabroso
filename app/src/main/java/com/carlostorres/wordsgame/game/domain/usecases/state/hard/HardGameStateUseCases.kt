package com.carlostorres.wordsgame.game.domain.usecases.state.hard

class HardGameStateUseCases(
    val readHardGameStateUseCase: ReadHardGameStateUseCase,
    val saveHardGameStateUseCase: SaveHardGameStateUseCase,
    val clearHardGameStateUseCase: ClearHardGameStateUseCase
)