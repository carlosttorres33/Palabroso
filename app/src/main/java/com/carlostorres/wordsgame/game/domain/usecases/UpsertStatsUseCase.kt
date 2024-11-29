package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.domain.repository.StatsRepo
import javax.inject.Inject

class UpsertStatsUseCase @Inject constructor(
    private val statsRepo: StatsRepo
) {
    operator fun invoke(stats: StatsEntity) {
        statsRepo.upsertStats(stats)
    }
}