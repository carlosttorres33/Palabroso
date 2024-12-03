package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.usecases.settings.CanAccessToAppUseCase
import com.carlostorres.wordsgame.game.domain.usecases.stats.ReadDailyStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.stats.UpdateDailyStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.words.GetRandomWordUseCase

class GameUseCases(
    val getRandomWordUseCase: GetRandomWordUseCase,
    val updateDailyStatsUseCase: UpdateDailyStatsUseCase,
    val readDailyStatsUseCase: ReadDailyStatsUseCase
)