package com.carlostorres.wordsgame.game.data.local

import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalWordsDataSource @Inject constructor(
    private val wordGameDao: WordGameDao
) {

    suspend fun getRandomWord(length: Int): String {
        return wordGameDao.getRandomWord(length).word
    }

    fun upsertStats(stat: StatsEntity) {
        wordGameDao.upsertStats(stat)
    }

    fun getGameModeStats(gameDifficult: String, win: Boolean): Flow<List<StatsEntity>> {
        return wordGameDao.getGameModeStats(gameDifficult, win)
    }

}