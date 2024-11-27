package com.carlostorres.wordsgame.game.domain.usecases

class GameUseCases(
    val getRandomWordUseCase: GetRandomWordUseCase,
    val canAccessToAppUseCase: CanAccessToAppUseCase,
    val updateDailyStatsUseCase: UpdateDailyStatsUseCase,
    val readDailyStatsUseCase: ReadDailyStatsUseCase
)