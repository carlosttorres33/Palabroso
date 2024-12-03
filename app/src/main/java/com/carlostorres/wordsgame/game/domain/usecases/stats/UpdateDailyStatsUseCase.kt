package com.carlostorres.wordsgame.game.domain.usecases.stats

import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import javax.inject.Inject

class UpdateDailyStatsUseCase @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(userDailyStats: UserDailyStats) {
        dataStoreOperations.saveDailyStats(userDailyStats)
    }

}