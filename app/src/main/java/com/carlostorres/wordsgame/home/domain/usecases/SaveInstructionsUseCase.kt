package com.carlostorres.wordsgame.home.domain.usecases

import com.carlostorres.wordsgame.home.domain.repository.DataStoreOperations
import javax.inject.Inject

class SaveInstructionsUseCase @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(seen : Boolean){
        dataStoreOperations.saveInstructionsState(seen)
    }

}