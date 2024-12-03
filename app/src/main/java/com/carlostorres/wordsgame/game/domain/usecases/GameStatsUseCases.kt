package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.usecases.stats.GetGameModeStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.stats.UpsertStatsUseCase

class GameStatsUseCases(
    val upsertStatsUseCase: UpsertStatsUseCase,
    val getGameModeStatsUseCase: GetGameModeStatsUseCase
)