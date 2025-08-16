package com.carlostorres.wordsgame.game.domain.usecases.easy

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.presentation.easy.EasyState

class ReadEasyGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(): EasyState? {
        return dataStoreOperations.loadEasyGameState()
    }

}