package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.usecases.settings.CanAccessToAppUseCase
import com.carlostorres.wordsgame.game.domain.usecases.settings.ReadAccessToAppDataStore
import com.carlostorres.wordsgame.game.domain.usecases.settings.SaveAccessToAppDataStore

class MenuUseCases(
    val canAccessToAppUseCase: CanAccessToAppUseCase,
    val saveAccessToAppUseCase: SaveAccessToAppDataStore,
    val readAccessToAppUseCase: ReadAccessToAppDataStore
)