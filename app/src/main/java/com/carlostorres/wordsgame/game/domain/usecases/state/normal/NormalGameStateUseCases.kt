package com.carlostorres.wordsgame.game.domain.usecases.state.normal

class NormalGameStateUseCases(
    val saveNormalGameStateUseCase: SaveNormalGameStateUseCase,
    val readNormalGameStateUseCase: ReadNormalGameStateUseCase,
    val clearNormalGameStateUseCase: ClearNormalGameStateUseCase
)