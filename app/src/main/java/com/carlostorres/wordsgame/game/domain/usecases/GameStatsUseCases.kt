package com.carlostorres.wordsgame.game.domain.usecases

class GameStatsUseCases(
    val upsertStatsUseCase: UpsertStatsUseCase,
    val getGameModeStatsUseCase: GetGameModeStatsUseCase
)