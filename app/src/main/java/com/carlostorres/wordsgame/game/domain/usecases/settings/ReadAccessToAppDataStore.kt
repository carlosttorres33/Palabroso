package com.carlostorres.wordsgame.game.domain.usecases.settings

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import kotlinx.coroutines.flow.Flow

class ReadAccessToAppDataStore(
    private val dataStoreOperations: DataStoreOperations
) {

    operator fun invoke() : Flow<Boolean> {
        return dataStoreOperations.readCanAccessToApp()
    }

}