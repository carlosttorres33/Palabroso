package com.carlostorres.wordsgame.game.domain.usecases.easy

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations

class ClearEasyGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke() {
        dataStoreOperations.clearEasyGameState()
    }

}