package com.carlostorres.wordsgame.game.domain.usecases.stats

import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.domain.repository.StatsRepo

class GetAllStatsUseCase(private val statsRepo: StatsRepo) {
    operator fun invoke(): List<StatsEntity> {
        return statsRepo.getAllStats()
    }
}