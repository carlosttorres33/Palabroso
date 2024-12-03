package com.carlostorres.wordsgame.game.domain.usecases.settings

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadInstructionsUseCase @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) {

    operator fun invoke(): Flow<Boolean> = dataStoreOperations.readInstructionsState()

}