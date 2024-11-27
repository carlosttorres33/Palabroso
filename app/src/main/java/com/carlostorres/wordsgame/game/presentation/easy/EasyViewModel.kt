package com.carlostorres.wordsgame.game.presentation.easy

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.game.data.model.TryInfo
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.domain.usecases.GameUseCases
import com.carlostorres.wordsgame.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.utils.Constants.EASY_WORD_LENGTH
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.keyboardCreator
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EasyViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCases: GameUseCases
) : ViewModel() {

    var state by mutableStateOf(EasyState())
        private set

    val dailyStats: Flow<UserDailyStats> = useCases.readDailyStatsUseCase()

    fun updateDailyStats(stats: UserDailyStats){
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateDailyStatsUseCase(stats)
        }
    }

    fun setUpGame(trie : Int){

        viewModelScope.launch(Dispatchers.IO) {

            state = state.copy(gameSituation = GameSituations.GameLoading)
            resetGame()

            try {

                Log.d("EasyViewModel", "Try number: $trie")

                val word = useCases.getRandomWordUseCase(
                    wordsTried = state.secretWordsList,
                    wordLength = EASY_WORD_LENGTH,
                    dayTries = trie
                )

                if (word.isNotEmpty()){
                    state = state.copy(
                        secretWord = word,
                        gameSituation = GameSituations.GameInProgress,
                        secretWordsList = state.secretWordsList.plus(word)
                    )
                }

            }catch (e : Exception){
                Log.e("EasyViewModel", "Error" + e.message.toString())
                //Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                state = state.copy(
                    gameSituation = GameSituations.GameError(e.message ?: "Error desconocido")
                )
            }

        }

    }

    private fun onAcceptClick(){

        state = state.copy(secretWordsList = state.wordsTried.plus(state.inputText))

        validateIfWordContainsLetter()

        if (state.inputText.uppercase() == state.secretWord.uppercase()){
            state = state.copy(
                gameSituation = GameSituations.GameWon,
                gameWinsCount = state.gameWinsCount + 1
            )
        }else if (state.tryNumber >= 3){
            state = state.copy(
                gameSituation = GameSituations.GameLost,
                gameLostCount = state.gameLostCount + 1
            )
        }

        Log.d("EasyViewModel", "Result: ${state.inputText.uppercase()} == ${state.secretWord.uppercase()}")

        when(state.tryNumber){
            0 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = ""
                )
            }

            1 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = ""
                )
            }

            2 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = ""
                )
            }

            3 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    inputText = ""
                )
            }

            else -> {
                resetGame()
            }

        }

    }

    fun onEvent(event: EasyEvents){
        when(event){
            EasyEvents.OnAcceptClick -> {
                onAcceptClick()
            }
            EasyEvents.OnDeleteClick -> {
                state = state.copy(
                    inputText = state.inputText.dropLast(1)
                )
            }
            is EasyEvents.OnInputTextChange -> {
                state = state.copy(
                    inputText = state.inputText + event.inputText
                )
            }
        }
    }

    private fun validateIfWordContainsLetter(){

        val result = mutableListOf<Pair<String, WordCharState>>()

        for (i in state.secretWord.indices) {
            if (state.secretWord[i].uppercase() == state.inputText[i].uppercase()) {
                result.add(Pair(state.inputText[i].toString(), WordCharState.IsOnPosition))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputText[i].uppercase()) it.copy(type = ButtonType.IsOnPosition) else it
                    }
                )
            } else if (state.secretWord.uppercase()
                    .contains(state.inputText[i].uppercase())
            ) {
                result.add(Pair(state.inputText[i].toString(), WordCharState.IsOnWord))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputText[i].uppercase()) it.copy(type = ButtonType.IsOnWord) else it
                    }
                )
            } else {
                result.add(Pair(state.inputText[i].toString(), WordCharState.IsNotInWord))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputText[i].uppercase()) it.copy(type = ButtonType.IsNotInWord) else it
                    }
                )
            }
        }

        Log.d("EasyViewModel", "SecretWord: $result")

        when(state.tryNumber){
            0 -> {
                state = state.copy(intento1 = TryInfo(state.inputText, result))
            }
            1 -> {
                state = state.copy(intento2= TryInfo(state.inputText, result))
            }
            2 -> {
                state = state.copy(intento3 = TryInfo(state.inputText, result))
            }
            3 -> {
                state = state.copy(intento4 = TryInfo(state.inputText, result))
            }
            else ->{

            }
        }

    }

    private fun resetGame(){
        state = state.copy(
            inputText = "",
            tryNumber = 0,
            intento1 = TryInfo(),
            intento2= TryInfo(),
            intento3 = TryInfo(),
            intento4 = TryInfo(),
            isGameWon = null,
            secretWord = "",
            keyboard = keyboardCreator(),
            wordsTried = emptyList()
        )
    }

    fun showInterstitial(activity: Activity, trie: Int){

        state = state.copy(
            gameSituation = GameSituations.GameLoading
        )

        loadInterstitial(activity){ interstitialAd ->
            if(interstitialAd != null){

                interstitialAd.show(activity)
                interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback(){
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        setUpGame(trie)
                    }
                }

            }else{
                Toast.makeText(context, "Te Salvaste del anuncio :c", Toast.LENGTH_SHORT).show()
                setUpGame(trie)
                Log.e("EasyViewModel", "Ad is null")

            }
        }

    }

    private fun loadInterstitial(activity: Activity, callback : (InterstitialAd?) -> Unit ){

        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            context.getString(R.string.ad_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback(){

                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    Log.e("EasyViewModel", "Error loading ad: ${error.message}")
                    callback(null)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    callback(interstitialAd)
                }

            }
        )

    }

}