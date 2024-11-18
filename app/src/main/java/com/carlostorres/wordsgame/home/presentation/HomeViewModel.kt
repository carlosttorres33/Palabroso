package com.carlostorres.wordsgame.home.presentation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.home.domain.repository.CanAccessToAppUseCase
import com.carlostorres.wordsgame.home.domain.usecases.HomeUseCases
import com.carlostorres.wordsgame.home.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.home.ui.components.keyboard.KeyboardChar
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases
) : ViewModel() {

    var state by mutableStateOf(HomeState())
        private set

    private val _seenInstructions = MutableStateFlow(true)
    val seenInstructions : StateFlow<Boolean> = _seenInstructions

    init {
        checkUserVersion()
        readInstructions()
    }

    private fun checkUserVersion(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCases.canAccessToAppUseCase()
            Log.d("Version", result.toString())
            state = state.copy(
                blockVersion = !result
            )
        }
    }

    private fun readInstructions(){
        viewModelScope.launch(Dispatchers.IO) {
            _seenInstructions.value = useCases.readInstructionsUseCase().stateIn(viewModelScope).value
            state = state.copy(
                seenInstructions = useCases.readInstructionsUseCase().stateIn(viewModelScope).value
            )
        }
    }

    fun saveSeenInstructionsState(seen : Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveInstructionsUseCase(seen)
        }
    }

    fun seeInstructions(seen: Boolean){
        state = state.copy(
            seenInstructions = seen
        )
    }

    fun setUpGame() {

        viewModelScope.launch(Dispatchers.IO) {

            state = state.copy(
                gameSituation = GameSituations.GameLoading
            )

            resetGame()

            try {

                val word = useCases.getRandomWordUseCase(state.wordsList)

                if (word.isNotEmpty()) {
                    state = state.copy(
                        actualSecretWord = word,
                        gameSituation = GameSituations.GameInProgress,
                        wordsList = state.wordsList.plus(word)
                    )
                }

            } catch (e: Exception) {

                Log.d("Error", e.message.toString())

                state = state.copy(
                    gameSituation = GameSituations.GameLost
                )

            }

        }


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
            } else if (state.actualSecretWord.uppercase()
                    .contains(state.inputText[i].uppercase())
            ) {
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

    private fun onAcceptClick() {

        state = state.copy(wordsTried = state.wordsTried.plus(state.inputText))

        val resultado = validateIfWordContainsLetter()

        Log.d(
            "secretWord",
            "${state.inputText.uppercase()} == ${state.actualSecretWord.uppercase()}"
        )

        if (state.inputText.uppercase() == state.actualSecretWord.uppercase()) {
            state = state.copy(
                gameSituation = GameSituations.GameWon,
                gameWinsCount = state.gameWinsCount + 1
            )
        } else if (state.tryNumber >= 4) {
            state = state.copy(
                gameSituation = GameSituations.GameLost,
                gameLostCount = state.gameLostCount + 1
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

    fun showInterstitial(activity: Activity){

        state = state.copy(
            gameSituation = GameSituations.GameLoading
        )

        loadInterstitial(activity){ interstitialAd ->
            if(interstitialAd != null){

                interstitialAd.show(activity)
                interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback(){
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        setUpGame()
                    }
                }

            }else{
                setUpGame()
                Log.d("Ad Error", "Ad is null")

            }
        }

    }

    private fun loadInterstitial(activity: Activity, callback : (InterstitialAd?) -> Unit ){

        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            "ca-app-pub-8184827769738877/6817794094",
            adRequest,
            object : InterstitialAdLoadCallback(){

                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    Log.d("Ad Error", "Error: ${error.message}")
                    callback(null)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    callback(interstitialAd)
                }

            }
        )

    }

    private fun resetGame() {

        state = state.copy(
            tryNumber = 0,
            inputText = "",
            intento1 = TryInfo(),
            intento2 = TryInfo(),
            intento3 = TryInfo(),
            intento4 = TryInfo(),
            intento5 = TryInfo(),
            isGameWon = false,
            isGameLost = false,
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
            ),
            actualSecretWord = "",
            wordsTried = emptyList()
        )
    }


}