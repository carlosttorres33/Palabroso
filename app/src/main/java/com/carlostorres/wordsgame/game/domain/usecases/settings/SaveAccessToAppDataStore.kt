package com.carlostorres.wordsgame.game.domain.usecases.settings

import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations

class SaveAccessToAppDataStore(
    private val dataStoreOperations: DataStoreOperations
) {

    suspend operator fun invoke(canAccess: Boolean) {
        dataStoreOperations.saveCanAccessToApp(canAccess)
    }

}