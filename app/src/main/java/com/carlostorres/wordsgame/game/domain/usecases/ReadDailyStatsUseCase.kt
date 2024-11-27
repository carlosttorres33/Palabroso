package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadDailyStatsUseCase @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) {

    operator fun invoke(): Flow<UserDailyStats> = dataStoreOperations.readDailyStats()

}