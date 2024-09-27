package com.carlostorres.wordsgame.home.domain.usecases

class HomeUseCases(
    val getAllWordsUseCase: GetAllWordsUseCase,
    val upsertAllWordsUseCase: UpsertAllWordsUseCase,
    val getRandomWordUseCase: GetRandomWordUseCase
)