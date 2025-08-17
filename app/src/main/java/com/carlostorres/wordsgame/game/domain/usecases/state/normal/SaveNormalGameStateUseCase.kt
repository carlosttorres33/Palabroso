package com.carlostorres.wordsgame.game.domain.usecases.state.normal

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.presentation.normal.NormalState

class SaveNormalGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(state: NormalState) {
        dataStoreOperations.saveNormalGameState(state)
    }

}