package com.carlostorres.wordsgame.game.presentation.normal

import com.carlostorres.wordsgame.game.data.model.TryInfo
import com.carlostorres.wordsgame.ui.components.keyboard.KeyboardChar
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.keyboardCreator

data class NormalState(

    val showLetterHintDialog : Boolean = false,
    val showKeyboardHintDialog : Boolean = false,
    val userCoins : Int = 0,

    val inputList : List<Char?> = (1..5).map { null },
    val indexFocused : Int = 0,

    val lettersHintsRemaining : Int = 1,
    val keyboardHintsRemaining : Int = 1,

    val showCoinsDialog : Boolean = false,

    val indexesGuessed : List<Int> = emptyList(),

    val tryNumber: Int = 0,
    val intento1 : TryInfo = TryInfo(),
    val intento2 : TryInfo = TryInfo(),
    val intento3 : TryInfo = TryInfo(),
    val intento4 : TryInfo = TryInfo(),
    val intento5 : TryInfo = TryInfo(),
    val isGameWon : Boolean? = null,

    val gameSituation : GameSituations = GameSituations.GameInProgress,
    val secretWord : String = "",
    val secretWordsList : List<String> = emptyList(),
    val keyboard : List<KeyboardChar> = keyboardCreator(),
    val gameWinsCount : Int = 0,
    val gameLostCount : Int = 0,
    val wordsTried : List<String> = emptyList()
)