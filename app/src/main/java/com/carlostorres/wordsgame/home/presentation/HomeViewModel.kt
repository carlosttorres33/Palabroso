package com.carlostorres.wordsgame.home.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel() {

    val secretWord = "papas"
    var state by mutableStateOf(HomeState())
        private set

    fun validateIfWordContainsLetter(): List<Pair<String, WordCharState>> {

        val resultado = mutableListOf<Pair<String, WordCharState>>()

        for (i in secretWord.indices) {
            if (secretWord[i].uppercase() == state.inputText[i].uppercase()) {
                resultado.add(Pair(state.inputText[i].toString(), WordCharState.IsOnPosition))
            } else if (secretWord.uppercase().contains(state.inputText[i].uppercase())) {
                resultado.add(Pair(state.inputText[i].toString(), WordCharState.IsOnWord))
            } else {
                resultado.add(Pair(state.inputText[i].toString(), WordCharState.IsNotInWord))
            }
        }

        Log.d("secretWord", "$resultado")

        return resultado

    }

    fun onAcceptClick() {

        val resultado = validateIfWordContainsLetter()

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

    fun resetGame(){

        state = HomeState()

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


}