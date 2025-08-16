package com.carlostorres.wordsgame.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class GameSituations{
    @Serializable
    object GameWon : GameSituations()
    @Serializable
    object GameLost : GameSituations()
    @Serializable
    object GameInProgress : GameSituations()
    @Serializable
    object GameLoading : GameSituations()
    @Serializable
    data class GameError(val errorMessage : String) : GameSituations()
}