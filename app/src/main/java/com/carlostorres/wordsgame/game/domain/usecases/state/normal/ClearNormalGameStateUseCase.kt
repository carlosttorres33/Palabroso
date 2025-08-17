package com.carlostorres.wordsgame.game.domain.usecases.state.normal

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations

class ClearNormalGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke() {
        dataStoreOperations.clearNormalGameState()
    }

}