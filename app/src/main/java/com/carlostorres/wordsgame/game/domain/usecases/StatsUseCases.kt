package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.usecases.stats.GetAllStatsUseCase

class StatsUseCases(
    val getAllStatsUseCase: GetAllStatsUseCase,
)