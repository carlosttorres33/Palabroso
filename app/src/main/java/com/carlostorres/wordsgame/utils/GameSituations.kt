package com.carlostorres.wordsgame.utils

sealed class GameSituations{
    object GameWon : GameSituations()
    object GameLost : GameSituations()
    object GameInProgress : GameSituations()
    object GameLoading : GameSituations()
    data class GameError(val errorMessage : String) : GameSituations()
}