package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.domain.repository.StatsRepo
import com.carlostorres.wordsgame.ui.components.GameDifficult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGameModeStatsUseCase @Inject constructor(
    private val statsRepo: StatsRepo
) {
    operator fun invoke(difficult: String, win : Boolean) : Flow<Int> {
        return statsRepo.getGameModeStats(difficult, win)
    }
}