package com.carlostorres.wordsgame.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.home.domain.usecases.HomeUseCases
import com.carlostorres.wordsgame.home.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.home.ui.components.keyboard.KeyboardChar
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases
) : ViewModel() {

    //val secretWord = "papas"
    var state by mutableStateOf(HomeState())
        private set

//    init {
//        setUpGame()
//    }

    fun setUpGame() {

        resetGame()

        state = state.copy(
            gameSituation = GameSituations.GameLoading
        )

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val word = useCases.getRandomWordUseCase(state.wordsList)

                if (word.isNotEmpty()){
                    state = state.copy(
                        actualSecretWord = word,
                        gameSituation = GameSituations.GameInProgress,
                        wordsList = state.wordsList.plus(word)
                    )
                }

//                val words = useCases.getAllWordsUseCase().map { it.word }
//
//                if (words.isEmpty()){
//                    useCases.upsertAllWordsUseCase()
//                }
//
//                words.forEach { palabra ->
//                    if (!state.wordsList.contains(palabra)) {
//                        state = state.copy(
//                            wordsList = state.wordsList.plus(palabra),
//                            actualSecretWord = palabra,
//                            gameSituation = GameSituations.GameInProgress
//                        )
//                        return@forEach
//                    }
//                }

            } catch (e: Exception) {


            }

        }


    }

    fun validarPalabrasEnLista(palabras: List<String>, lista: List<String>): Boolean {
        return palabras.all { palabra -> lista.any { it.contains(palabra) } }
    }

    private fun validateIfWordContainsLetter(): List<Pair<String, WordCharState>> {

        val resultado = mutableListOf<Pair<String, WordCharState>>()

        for (i in state.actualSecretWord.indices) {
            if (state.actualSecretWord[i].uppercase() == state.inputText[i].uppercase()) {
                resultado.add(Pair(state.inputText[i].toString(), WordCharState.IsOnPosition))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputText[i].uppercase()) it.copy(type = ButtonType.IsOnPosition) else it
                    }
                )
            } else if (state.actualSecretWord.uppercase().contains(state.inputText[i].uppercase())) {
                resultado.add(Pair(state.inputText[i].toString(), WordCharState.IsOnWord))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputText[i].uppercase()) it.copy(type = ButtonType.IsOnWord) else it
                    }
                )
            } else {
                resultado.add(Pair(state.inputText[i].toString(), WordCharState.IsNotInWord))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputText[i].uppercase()) it.copy(type = ButtonType.IsNotInWord) else it
                    }
                )
            }
        }

        Log.d("secretWord", "$resultado")

        return resultado

    }

    fun checkInputEqualsSecretWord(): Boolean {
        for (i in state.actualSecretWord.indices) {
            if (state.actualSecretWord[i].uppercase() != state.inputText[i].uppercase()) {
                return false
            }
        }
        return true
    }

    private fun onAcceptClick() {

        val resultado = validateIfWordContainsLetter()

        Log.d("secretWord", "${state.inputText.uppercase()} == ${state.actualSecretWord.uppercase()}")

        if (state.inputText.uppercase() == state.actualSecretWord.uppercase()) {
            state = state.copy(
                gameSituation = GameSituations.GameWon
            )
        }else if (state.tryNumber >= 4) {
            state = state.copy(
                gameSituation = GameSituations.GameLost
            )
            return
        }

        when (state.tryNumber) {
            0 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = "",
                    intento1 = state.intento1.copy(
                        word = state.inputText,
                        resultado = resultado
                    )
                )
            }

            1 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = "",
                    intento2 = state.intento2.copy(
                        word = state.inputText,
                        resultado = resultado
                    )
                )
            }

            2 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = "",
                    intento3 = state.intento3.copy(
                        word = state.inputText,
                        resultado = resultado
                    )
                )
            }

            3 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = "",
                    intento4 = state.intento4.copy(
                        word = state.inputText,
                        resultado = resultado
                    )
                )
            }

            4 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = "",
                    intento5 = state.intento5.copy(
                        word = state.inputText,
                        resultado = resultado
                    )
                )
            }

            else -> {
                resetGame()
            }

        }

    }

    fun onEvent(event: HomeEvents) {

        when (event) {
            is HomeEvents.OnInputTextChange -> {
                state = state.copy(
                    inputText = state.inputText + event.inputText
                )
            }

            is HomeEvents.OnAcceptClick -> {
                onAcceptClick()
            }

            is HomeEvents.OnDeleteClick -> {
                state = state.copy(
                    inputText = state.inputText.dropLast(1)
                )
            }
        }

    }

    private fun resetGame() {

        state = state.copy(
            inputText = "",
            tryNumber = 0,
            intento1 = TryInfo(),
            intento2 = TryInfo(),
            intento3 = TryInfo(),
            intento4 = TryInfo(),
            intento5 = TryInfo(),
            isGameWon = false,
            isGameLost = false,
            actualSecretWord = "",
            keyboard = listOf(
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
            )
        )

    }


}