package com.carlostorres.wordsgame.game.presentation.hard

import com.carlostorres.wordsgame.game.data.model.TryInfo
import com.carlostorres.wordsgame.ui.components.keyboard.KeyboardChar
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.keyboardCreator

data class HardState(

    val inputList : List<Char?> = (1..6).map { null },
    val indexFocused : Int = 0,

    //Variable que almacena el numero de intento en el que va el usuario
    val tryNumber : Int = 0,

    val lettersHintsRemaining : Int = 1,
    val keyboardHintsRemaining : Int = 1,

    val indexesGuessed : List<Int> = emptyList(),

    //region informacion de los intentos
    val intento1 : TryInfo = TryInfo(),
    val intento2 : TryInfo = TryInfo(),
    val intento3 : TryInfo = TryInfo(),
    val intento4 : TryInfo = TryInfo(),
    val intento5 : TryInfo = TryInfo(),
    //endregion

    //true = win, false = lost, null = in progress
    val isGameWon : Boolean? = null,

    //Controla el estado de la pantalla
    val gameSituation : GameSituations = GameSituations.GameInProgress,

    //Palabra secreta a adivinar
    val secretWord : String = "",

    //Lista de palabras secretas jugadas
    val secretWordsList : List<String> = emptyList(),

    val keyboard : List<KeyboardChar> = keyboardCreator(),

    val wordsTried : List<String> = emptyList()

)