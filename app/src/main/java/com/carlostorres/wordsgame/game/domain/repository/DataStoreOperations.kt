package com.carlostorres.wordsgame.game.domain.repository

import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.presentation.easy.EasyState
import com.carlostorres.wordsgame.game.presentation.hard.HardState
import com.carlostorres.wordsgame.game.presentation.normal.NormalState
import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveDailyStats(stats: UserDailyStats)

    fun readDailyStats(): Flow<UserDailyStats>

    suspend fun saveInstructionsState(seen: Boolean)

    fun readInstructionsState(): Flow<Boolean>

    suspend fun saveCanAccessToApp(canAccess: Boolean)

    fun readCanAccessToApp(): Flow<Boolean>

    suspend fun updateCoins(coins: Int)

    fun getCoins(): Flow<Int>

    //region Save and load game state
    //**Easy
    suspend fun saveEasyGameState(state: EasyState)
    suspend fun loadEasyGameState(): EasyState?
    suspend fun clearEasyGameState()

    //**Normal
    suspend fun saveNormalGameState(state: NormalState)
    suspend fun loadNormalGameState(): NormalState?
    suspend fun clearNormalGameState()

    //**Hard
    suspend fun saveHardGameState(state: HardState)
    suspend fun loadHardGameState(): HardState?
    suspend fun clearHardGameState()
    //endregion

}