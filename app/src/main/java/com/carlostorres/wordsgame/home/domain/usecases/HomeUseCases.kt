package com.carlostorres.wordsgame.home.domain.usecases

import com.carlostorres.wordsgame.home.domain.repository.CanAccessToAppUseCase

class HomeUseCases(
    val getAllWordsUseCase: GetAllWordsUseCase,
    val upsertAllWordsUseCase: UpsertAllWordsUseCase,
    val getRandomWordUseCase: GetRandomWordUseCase,
    val saveInstructionsUseCase: SaveInstructionsUseCase,
    val readInstructionsUseCase: ReadInstructionsUseCase,
    val canAccessToAppUseCase: CanAccessToAppUseCase
)