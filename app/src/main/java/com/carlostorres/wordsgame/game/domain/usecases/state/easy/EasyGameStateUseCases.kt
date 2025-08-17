package com.carlostorres.wordsgame.game.domain.usecases.state.easy

class EasyGameStateUseCases (
    val saveEasyGameStateUseCase: SaveEasyGameStateUseCase,
    val readEasyGameStateUseCase: ReadEasyGameStateUseCase,
    val clearEasyGameStateUseCase: ClearEasyGameStateUseCase
)