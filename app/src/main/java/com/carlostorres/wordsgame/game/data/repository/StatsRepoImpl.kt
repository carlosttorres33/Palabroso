package com.carlostorres.wordsgame.game.data.repository

import android.util.Log
import com.carlostorres.wordsgame.game.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.domain.repository.StatsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StatsRepoImpl @Inject constructor(
    private val localWordDataSource: LocalWordsDataSource
) : StatsRepo {

    override fun upsertStats(stat: StatsEntity) {
        localWordDataSource.upsertStats(stat)
    }

    override fun getGameModeStats(gameDifficult: String, win: Boolean): Flow<Int> {
        return localWordDataSource.getGameModeStats(gameDifficult, win)
    }

}