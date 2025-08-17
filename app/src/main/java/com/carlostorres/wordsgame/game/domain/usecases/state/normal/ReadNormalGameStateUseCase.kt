package com.carlostorres.wordsgame.game.domain.usecases.state.normal

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.presentation.normal.NormalState

class ReadNormalGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(): NormalState? {
        return dataStoreOperations.loadNormalGameState()
    }

}