package com.carlostorres.wordsgame.game.presentation.hard

import com.carlostorres.wordsgame.game.data.model.TryInfo
import com.carlostorres.wordsgame.game.presentation.WordModel
import com.carlostorres.wordsgame.game.presentation.normal.NormalState
import com.carlostorres.wordsgame.ui.components.keyboard.KeyboardChar
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.keyboardCreator
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class HardState(

    val showLetterHintDialog : Boolean = false,
    val showKeyboardHintDialog : Boolean = false,

    val inputList : List<Char?> = (1..6).map { null },
    val indexFocused : Int = 0,

    //Variable que almacena el numero de intento en el que va el usuario
    val tryNumber : Int = 0,

    val lettersHintsRemaining : Int = 1,
    val keyboardHintsRemaining : Int = 1,

    val showCoinsDialog : Boolean = false,
    val showReportWordDialog : Boolean = false,
    val isAlreadyReported : Boolean = false,

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
    val secretWord : WordModel = WordModel("", 0),

    //Lista de palabras secretas jugadas
    val secretWordsList : List<String> = emptyList(),

    val keyboard : List<KeyboardChar> = keyboardCreator(),

    val wordsTried : List<String> = emptyList()

){

    fun toJson(): String = Json.encodeToString(this)

    companion object {
        fun fromJson(json: String): HardState? = try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            null
        }
    }

}