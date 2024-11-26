package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.repository.CanAccessToAppUseCase

class GameUseCases(
    val getAllWordsUseCase: GetAllWordsUseCase,
    val upsertAllWordsUseCase: UpsertAllWordsUseCase,
    val getRandomWordUseCase: GetRandomWordUseCase,
    val saveInstructionsUseCase: SaveInstructionsUseCase,
    val readInstructionsUseCase: ReadInstructionsUseCase,
    val canAccessToAppUseCase: CanAccessToAppUseCase
)