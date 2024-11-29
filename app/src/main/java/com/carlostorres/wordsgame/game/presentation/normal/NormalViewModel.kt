package com.carlostorres.wordsgame.game.presentation.normal

import android.app.Activity
import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.data.model.TryInfo
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.domain.usecases.GameStatsUseCases
import com.carlostorres.wordsgame.game.domain.usecases.GameUseCases
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.utils.Constants.NORMAL_WORD_LENGTH
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.difficultToString
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class NormalViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCases: GameUseCases,
    private val gameStatsUseCases: GameStatsUseCases
) : ViewModel() {

    var state by mutableStateOf(NormalState())
        private set

    private val _userDailyStats = MutableStateFlow<UserDailyStats>(
        UserDailyStats(
            easyGamesPlayed = 0,
            normalGamesPlayed = 0,
            hardGamesPlayed = 0,
            lastPlayedDate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        )
    )
    val userDailyStats: StateFlow<UserDailyStats> = _userDailyStats.asStateFlow()

    val gameWinsCount: Flow<Int> = gameStatsUseCases.getGameModeStatsUseCase(
        difficult = difficultToString(GameDifficult.Normal),
        win = true
    )

    val gameLostCount: Flow<Int> = gameStatsUseCases.getGameModeStatsUseCase(
        difficult = difficultToString(GameDifficult.Normal),
        win = false
    )

    init {
        viewModelScope.launch {
            useCases.readDailyStatsUseCase().collect { stats ->
                _userDailyStats.value = stats
            }
        }
    }

    private fun updateDailyStats(win: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            gameStatsUseCases.upsertStatsUseCase(
                StatsEntity(
                    wordGuessed = state.secretWord,
                    gameDifficult = difficultToString(GameDifficult.Normal),
                    win = win,
                    attempts = state.tryNumber
                )
            )
        }
    }

    fun setUpGame() {

        viewModelScope.launch(Dispatchers.IO) {

            state = state.copy(
                gameSituation = GameSituations.GameLoading
            )

            resetGame()

            try {

                val word = useCases.getRandomWordUseCase(
                    wordsTried = state.secretWordsList,
                    wordLength = NORMAL_WORD_LENGTH,
                    dayTries = userDailyStats.value.normalGamesPlayed
                )

                if (word.isNotEmpty()) {
                    state = state.copy(
                        secretWord = word,
                        gameSituation = GameSituations.GameInProgress,
                        secretWordsList = state.secretWordsList.plus(word)
                    )
                }

            } catch (e: Exception) {

                Log.d("Error", e.message.toString())

                state = state.copy(
                    gameSituation = GameSituations.GameError(e.message ?: "Error desconocido")
                )

            }

        }


    }

    private suspend fun increaseNormalGamesPlayed() {
        useCases.updateDailyStatsUseCase(
            _userDailyStats.value.copy(
                normalGamesPlayed = _userDailyStats.value.normalGamesPlayed + 1
            )
        )
    }

    private fun onAcceptClick() = viewModelScope.launch {

        state = state.copy(wordsTried = state.wordsTried.plus(state.inputText))

        val resultado = validateIfWordContainsLetter()

        if (state.inputText.uppercase() == state.secretWord.uppercase()) {
            state = state.copy(
                gameSituation = GameSituations.GameWon,
                gameWinsCount = state.gameWinsCount + 1
            )
            increaseNormalGamesPlayed()
            updateDailyStats(true)
        } else if (state.tryNumber >= 4) {
            state = state.copy(
                gameSituation = GameSituations.GameLost,
                gameLostCount = state.gameLostCount + 1
            )
            increaseNormalGamesPlayed()
            updateDailyStats(false)
        }

        Log.d(
            "secretWord",
            "${state.inputText.uppercase()} == ${state.secretWord.uppercase()}"
        )

        when (state.tryNumber) {
            0 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento1 = state.intento1.copy(
                        word = state.inputText,
                        resultado = resultado
                    ),
                    inputText = "",
                )
            }

            1 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento2 = state.intento2.copy(
                        word = state.inputText,
                        resultado = resultado
                    ),
                    inputText = "",
                )
            }

            2 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento3 = state.intento3.copy(
                        word = state.inputText,
                        resultado = resultado
                    ),
                    inputText = "",
                )
            }

            3 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento4 = state.intento4.copy(
                        word = state.inputText,
                        resultado = resultado
                    ),
                    inputText = "",
                )
            }

            4 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento5 = state.intento5.copy(
                        word = state.inputText,
                        resultado = resultado
                    ),
                    inputText = "",
                )
            }

            else -> {
                resetGame()
            }
        }

    }


    private fun validateIfWordContainsLetter(): List<Pair<String, WordCharState>> {

        val resultado = mutableListOf<Pair<String, WordCharState>>()

        for (i in state.secretWord.indices) {
            if (state.secretWord[i].uppercase() == state.inputText[i].uppercase()) {
                resultado.add(Pair(state.inputText[i].toString(), WordCharState.IsOnPosition))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputText[i].uppercase()) it.copy(type = ButtonType.IsOnPosition) else it
                    }
                )
            } else if (state.secretWord.uppercase()
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

    fun onEvent(event: NormalEvents) {

        when (event) {
            is NormalEvents.OnInputTextChange -> {
                state = state.copy(
                    inputText = state.inputText + event.inputText
                )
            }

            is NormalEvents.OnAcceptClick -> {
                onAcceptClick()
            }

            is NormalEvents.OnDeleteClick -> {
                state = state.copy(
                    inputText = state.inputText.dropLast(1)
                )
            }
        }

    }

    fun showInterstitial(activity: Activity, navHome : () -> Unit, ifBack : Boolean = false) {

        state = state.copy(
            gameSituation = GameSituations.GameLoading
        )

        loadInterstitial(activity) { interstitialAd ->
            if (interstitialAd != null) {

                interstitialAd.show(activity)
                interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        if (ifBack || userDailyStats.value.normalGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                            navHome()
                        } else {
                            setUpGame()
                        }
                    }
                }

            } else {
                Toast.makeText(context, "Te Salvaste del anuncio :c", Toast.LENGTH_SHORT).show()
                if (ifBack || userDailyStats.value.normalGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                    navHome()
                } else {
                    setUpGame()
                }
                Log.d("Ad Error", "Ad is null")

            }
        }

    }

    private fun loadInterstitial(activity: Activity, callback: (InterstitialAd?) -> Unit) {

        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            context.getString(R.string.ad_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback() {

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
            inputText = "",
            tryNumber = 0,
            intento1 = TryInfo(),
            intento2 = TryInfo(),
            intento3 = TryInfo(),
            intento4 = TryInfo(),
            intento5 = TryInfo(),
            isGameWon = null,
            secretWord = "",
            keyboard = keyboardCreator(),
            wordsTried = emptyList()
        )
    }


}