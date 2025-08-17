package com.carlostorres.wordsgame.game.domain.usecases.state.hard

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.presentation.hard.HardState

class SaveHardGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(state: HardState) {
        dataStoreOperations.saveHardGameState(state)
    }

}