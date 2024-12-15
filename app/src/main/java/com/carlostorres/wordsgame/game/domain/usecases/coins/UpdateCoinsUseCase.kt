package com.carlostorres.wordsgame.game.domain.usecases.coins

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations

class UpdateCoinsUseCase (private val dataStoreOperations: DataStoreOperations) {

    suspend operator fun invoke(coins: Int) {
        dataStoreOperations.updateCoins(coins)
    }

}