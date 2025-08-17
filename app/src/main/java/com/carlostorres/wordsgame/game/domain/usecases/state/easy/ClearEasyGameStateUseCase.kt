package com.carlostorres.wordsgame.game.domain.usecases.state.easy

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations

class ClearEasyGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke() {
        dataStoreOperations.clearEasyGameState()
    }

}