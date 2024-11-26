package com.carlostorres.wordsgame.game.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveInstructionsState(seen: Boolean)

    fun readInstructionsState(): Flow<Boolean>

}