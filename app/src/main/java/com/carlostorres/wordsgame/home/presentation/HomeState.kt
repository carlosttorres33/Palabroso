package com.carlostorres.wordsgame.home.presentation

import com.carlostorres.wordsgame.home.data.local.model.WordEntity
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState

data class HomeState(

    val inputText: String = "",
    val tryNumber: Int = 0,
    val intento1 : TryInfo = TryInfo(),
    val intento2 : TryInfo = TryInfo(),
    val intento3 : TryInfo = TryInfo(),
    val intento4 : TryInfo = TryInfo(),
    val intento5 : TryInfo = TryInfo(),
    val isGameWon : Boolean = false,
    val isGameLost : Boolean = false,
    val gameSituation : GameSituations = GameSituations.GameInProgress,

    val actualSecretWord : String = "",
    val wordsList : List<String> = emptyList()

)

sealed class GameSituations{
    object GameWon : GameSituations()
    object GameLost : GameSituations()
    object GameInProgress : GameSituations()
    object GameLoading : GameSituations()
}

data class TryInfo(
    val word : String = "",
    val resultado : List<Pair<String, WordCharState>> = emptyList()
)