package com.carlostorres.wordsgame.home.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreOperations {

    suspend fun saveInstructionsState(seen: Boolean)

    fun readInstructionsState(): Flow<Boolean>

}