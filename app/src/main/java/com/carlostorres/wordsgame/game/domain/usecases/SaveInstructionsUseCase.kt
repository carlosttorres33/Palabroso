package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import javax.inject.Inject

class SaveInstructionsUseCase @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(seen : Boolean){
        dataStoreOperations.saveInstructionsState(seen)
    }

}