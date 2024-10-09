package com.carlostorres.wordsgame.home.domain.usecases

import com.carlostorres.wordsgame.home.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadInstructionsUseCase @Inject constructor(
    private val dataStoreOperations: DataStoreOperations
) {

    operator fun invoke(): Flow<Boolean> = dataStoreOperations.readInstructionsState()

}