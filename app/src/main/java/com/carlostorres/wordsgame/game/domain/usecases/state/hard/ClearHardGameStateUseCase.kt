package com.carlostorres.wordsgame.game.domain.usecases.state.hard

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations

class ClearHardGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke() {
        dataStoreOperations.clearHardGameState()
    }

}