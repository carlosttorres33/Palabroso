package com.carlostorres.wordsgame.game.domain.repository

import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveDailyStats(stats: UserDailyStats)

    fun readDailyStats(): Flow<UserDailyStats>

    suspend fun saveInstructionsState(seen: Boolean)

    fun readInstructionsState(): Flow<Boolean>

    suspend fun saveCanAccessToApp(canAccess: Boolean)

    fun readCanAccessToApp(): Flow<Boolean>

}