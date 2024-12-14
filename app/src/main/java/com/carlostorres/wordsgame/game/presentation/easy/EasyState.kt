package com.carlostorres.wordsgame.game.presentation.easy

import com.carlostorres.wordsgame.game.data.model.TryInfo
import com.carlostorres.wordsgame.ui.components.keyboard.KeyboardChar
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.keyboardCreator

data class EasyState(

    val showLetterHintDialog : Boolean = false,
    val showKeyboardHintDialog : Boolean = false,
    val userCoins : Int = 0,

    val inputList : List<Char?> = (1..4).map { null },
    val indexFocused : Int = 0, ////Podemos hacerlo nulo para liberar el Foco cuando ningun elemento de la lista esta vacio, solo si clickeamos algun elemento se hace focus ahi

    //Variable que almacena el numero de intento en el que va el usuario
    val tryNumber : Int = 0,

    val lettersHintsRemaining : Int = 1,
    val keyboardHintsRemaining : Int = 1,

    val showCoinsDialog : Boolean = false,

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

    val gameWinsCount : Int = 0,
    val gameLostCount : Int = 0,

    val wordsTried : List<String> = emptyList()

)