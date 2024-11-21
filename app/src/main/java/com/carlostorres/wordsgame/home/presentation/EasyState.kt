package com.carlostorres.wordsgame.home.presentation

import com.carlostorres.wordsgame.ui.components.keyboard.KeyboardChar
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState

data class EasyState(
    val blockVersion : Boolean = false,
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
    val wordsList : List<String> = emptyList(),
    val keyboard : List<KeyboardChar> = listOf(
        KeyboardChar("Q"),
        KeyboardChar("W"),
        KeyboardChar("E"),
        KeyboardChar("R"),
        KeyboardChar("T"),
        KeyboardChar("Y"),
        KeyboardChar("U"),
        KeyboardChar("I"),
        KeyboardChar("O"),
        KeyboardChar("P"),
        KeyboardChar("A"),
        KeyboardChar("S"),
        KeyboardChar("D"),
        KeyboardChar("F"),
        KeyboardChar("G"),
        KeyboardChar("H"),
        KeyboardChar("J"),
        KeyboardChar("K"),
        KeyboardChar("L"),
        KeyboardChar("Ñ"),
        KeyboardChar("Z"),
        KeyboardChar("X"),
        KeyboardChar("C"),
        KeyboardChar("V"),
        KeyboardChar("B"),
        KeyboardChar("N"),
        KeyboardChar("M")
    ),
    val gameWinsCount : Int = 0,
    val gameLostCount : Int = 0,
    val wordsTried : List<String> = emptyList(),
    val seenInstructions : Boolean = true
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