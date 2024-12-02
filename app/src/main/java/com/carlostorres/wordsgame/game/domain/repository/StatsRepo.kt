package com.carlostorres.wordsgame.game.domain.repository

import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import kotlinx.coroutines.flow.Flow

interface StatsRepo {

    suspend fun upsertStats(stat: StatsEntity)

    fun getGameModeStats(gameDifficult: String, win: Boolean): Flow<Int>

    fun getAllStats(): List<StatsEntity>

}