package com.carlostorres.wordsgame.game.domain.usecases.coins

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow

class GetCoinsUseCase (
    private val dataStoreOperations: DataStoreOperations
) {

    operator fun invoke(): Flow<Int> {
        return dataStoreOperations.getCoins()
    }

}