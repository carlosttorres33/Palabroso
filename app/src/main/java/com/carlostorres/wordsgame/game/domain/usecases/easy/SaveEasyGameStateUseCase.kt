package com.carlostorres.wordsgame.game.domain.usecases.easy

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.presentation.easy.EasyState

class SaveEasyGameStateUseCase(
    private val dataStoreOperations: DataStoreOperations
){

    suspend operator fun invoke(gameState: EasyState) {
        dataStoreOperations.saveEasyGameState(gameState)
    }

}