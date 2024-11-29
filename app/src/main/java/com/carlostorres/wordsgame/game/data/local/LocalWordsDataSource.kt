package com.carlostorres.wordsgame.game.data.local

import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalWordsDataSource @Inject constructor(
    private val wordGameDao: WordGameDao,
    private val statsGameDao: StatsGameDao
) {

    suspend fun getRandomWord(length: Int): String {
        return wordGameDao.getRandomWord(length).word
    }

    fun upsertStats(stat: StatsEntity) {
        statsGameDao.upsertStats(stat)
    }

    fun getGameModeStats(gameDifficult: String, win: Boolean): Flow<Int> {
        return statsGameDao.getGameModeStats(gameDifficult, win)
    }

}