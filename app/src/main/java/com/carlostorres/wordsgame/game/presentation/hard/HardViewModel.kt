package com.carlostorres.wordsgame.game.presentation.hard

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
import com.carlostorres.wordsgame.game.presentation.GameEvents
import com.carlostorres.wordsgame.game.presentation.easy.RewardedAdType
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.utils.Constants.EP_6_LETTERS
import com.carlostorres.wordsgame.utils.Constants.HARD_WORD_LENGTH
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.difficultToString
import com.carlostorres.wordsgame.utils.keyboardCreator
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
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
class HardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCases: GameUseCases,
    private val gameStatsUseCases: GameStatsUseCases
) : ViewModel() {

    var state by mutableStateOf(HardState())
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
        difficult = difficultToString(GameDifficult.Hard),
        win = true
    )

    val gameLostCount: Flow<Int> = gameStatsUseCases.getGameModeStatsUseCase(
        difficult = difficultToString(GameDifficult.Hard),
        win = false
    )

    init {
        viewModelScope.launch {
            useCases.readDailyStatsUseCase().collect { stats ->
                _userDailyStats.value = stats
            }
        }
    }

    private fun updateDailyStats(win: Boolean, tryNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Trackeo", "${state.tryNumber} en update")
            gameStatsUseCases.upsertStatsUseCase(
                StatsEntity(
                    wordGuessed = state.secretWord,
                    gameDifficult = difficultToString(GameDifficult.Hard),
                    win = win,
                    attempts = tryNumber
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
                    wordLength = HARD_WORD_LENGTH,
                    dayTries = userDailyStats.value.hardGamesPlayed,
                    group = EP_6_LETTERS
                )

                if (!word.isNullOrEmpty()) {
                    state = state.copy(
                        secretWord = word,
                        gameSituation = GameSituations.GameInProgress,
                        secretWordsList = state.secretWordsList.plus(word)
                    )
                } else {
                    state = state.copy(
                        gameSituation = GameSituations.GameError("Error Desconocido")
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

    private suspend fun increaseHardGamesPlayed() {
        useCases.updateDailyStatsUseCase(
            _userDailyStats.value.copy(
                hardGamesPlayed = _userDailyStats.value.hardGamesPlayed + 1
            )
        )
    }

    private fun onAcceptClick() = viewModelScope.launch {

        state = state.copy(
            wordsTried = state.wordsTried.plus(state.inputList.joinToString(""))
        )

        val resultado = validateIfWordContainsLetter()

        if (state.inputList.joinToString("").uppercase() == state.secretWord.uppercase()) {
            state = state.copy(
                gameSituation = GameSituations.GameWon
            )
            increaseHardGamesPlayed()
            updateDailyStats(true, state.tryNumber)
        } else if (state.tryNumber >= 4) {
            state = state.copy(
                gameSituation = GameSituations.GameLost
            )
            increaseHardGamesPlayed()
            updateDailyStats(false, state.tryNumber)
        }

        Log.d(
            "secretWord",
            "${state.inputList.joinToString("").uppercase()} == ${state.secretWord.uppercase()}"
        )

        when (state.tryNumber) {
            0 -> {
                Log.d("Trackeo", "${state.tryNumber} en WHEN")
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento1 = state.intento1.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    indexFocused = 0,
                    inputList = (1..6).map { null }
                )
            }

            1 -> {
                Log.d("Trackeo", "${state.tryNumber} en WHEN")
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento2 = state.intento2.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..6).map { null },
                    indexFocused = 0
                )
            }

            2 -> {
                Log.d("Trackeo", "${state.tryNumber} en WHEN")
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento3 = state.intento3.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..6).map { null },
                    indexFocused = 0
                )
            }

            3 -> {
                Log.d("Trackeo", "${state.tryNumber} en WHEN")
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento4 = state.intento4.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..6).map { null },
                    indexFocused = 0
                )
            }

            4 -> {
                Log.d("Trackeo", "${state.tryNumber} en WHEN")
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento5 = state.intento5.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..6).map { null },
                    indexFocused = 0
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
            if (state.secretWord[i].uppercase() == state.inputList[i]?.uppercase().orEmpty()) {
                resultado.add(Pair(state.inputList[i].toString(), WordCharState.IsOnPosition))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputList[i]?.uppercase().orEmpty()) it.copy(type = ButtonType.IsOnPosition) else it
                    }
                )
            } else if (state.secretWord.uppercase()
                    .contains(state.inputList[i]?.uppercase().orEmpty())
            ) {
                resultado.add(Pair(state.inputList[i].toString(), WordCharState.IsOnWord))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputList[i]?.uppercase().orEmpty()) it.copy(type = ButtonType.IsOnWord) else it
                    }
                )
            } else {
                resultado.add(Pair(state.inputList[i].toString(), WordCharState.IsNotInWord))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputList[i]?.uppercase().orEmpty()) it.copy(type = ButtonType.IsNotInWord) else it
                    }
                )
            }
        }

        Log.d("secretWord", "$resultado")

        return resultado

    }

    fun onEvent(event: GameEvents) {

        when (event) {

            is GameEvents.OnAcceptClick -> {
                onAcceptClick()
            }

            is GameEvents.OnFocusChange -> {
                state = state.copy(
                    indexFocused = event.index
                )
            }

            is GameEvents.OnKeyboardClick -> {
                state = state.copy(
                    inputList = state.inputList.mapIndexed { currentIndex, currentChar ->
                        if (currentIndex == event.index){
                            event.char
                        }else{
                            currentChar
                        }
                    }
                )
                Log.d("EasyViewModel", "InputList: ${state.inputList}")
                state = state.copy(
                    indexFocused = getNextFocusedIndex()
                )
            }

            GameEvents.OnKeyboardDeleteClick -> {
                val prevIndex = if (state.inputList[state.indexFocused] == null) state.indexFocused-1 else state.indexFocused
                state = state.copy(
                    indexFocused = if (state.inputList[state.indexFocused] == null){
                        getPreviousFocusedIndex()
                    } else {
                        state.indexFocused
                    },
                    inputList = state.inputList.mapIndexed { currentIndex, currentChar ->
                        if (currentIndex == prevIndex){
                            null
                        }else{
                            currentChar
                        }
                    }
                )
            }
        }

    }

    private fun getNextFocusedIndex() : Int {

        if (state.inputList.all { it == null }) return 0

        state.inputList.forEachIndexed { index, char ->

            if (index >= state.indexFocused){
                if (char == null) return index
            }

        }

        state.inputList.forEachIndexed { index, char ->
            if (char == null) return index
        }

        return state.indexFocused

    }

    private fun getPreviousFocusedIndex(): Int {
        return state.indexFocused.minus(1).coerceAtLeast(0) ?: 0
    }

    private fun disable4KeyboardLettersHint(){

        //get random index from keyboard list that doesnt contains secret word chars
        val randomIndex = (0..2). map { counterIndex ->
            var possibleIndex = (0 until state.keyboard.size).random()
            while (state.secretWord.contains(state.keyboard[possibleIndex].char) || state.keyboard[possibleIndex].type == ButtonType.IsNotInWord){
                possibleIndex = (0 until state.keyboard.size).random()
            }
            possibleIndex
        }

        state = state.copy(
            keyboard = state.keyboard.mapIndexed { index, keyboardChar ->
                if (randomIndex.contains(index)){
                    keyboardChar.copy(type = ButtonType.IsNotInWord)
                }else{
                    keyboardChar
                }
            },
            keyboardHintsRemaining = state.keyboardHintsRemaining - 1
        )

    }

    private fun getOneLetterWord() {

        val indexToShow = (0..3).random()

        state = state.copy(
            inputList = state.inputList.mapIndexed { currentIndex, currentChar ->
                if (currentIndex == indexToShow){
                    state.secretWord[indexToShow]
                }else{
                    currentChar
                }
            },
            lettersHintsRemaining = state.lettersHintsRemaining - 1
        )

    }

    fun showRewardedAd(activity: Activity, adType: RewardedAdType) {
        state = state.copy(gameSituation = GameSituations.GameLoading)
        loadRewardedAd(activity){ rewardedAd ->

            if (rewardedAd != null){

                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback(){
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        when(adType){
                            RewardedAdType.ONE_LETTER ->{
                                Log.d("EasyViewModel", "One letter hint")
                                getOneLetterWord()
                            }
                            RewardedAdType.KEYBOARD -> {
                                Log.d("EasyViewModel", "Keyboard hint")
                                disable4KeyboardLettersHint()
                            }
                        }
                        state = state.copy(gameSituation = GameSituations.GameInProgress)
                    }
                }

                rewardedAd.show(activity, OnUserEarnedRewardListener { rewardItem ->
                    val amount = rewardItem.amount
                })

            }else{
                Toast.makeText(context, "Por ahora no se puede mostrar el anuncio :c", Toast.LENGTH_SHORT).show()
                state = state.copy(gameSituation = GameSituations.GameInProgress)
            }

        }
    }

    private fun loadRewardedAd(activity: Activity, callback: (RewardedAd?) -> Unit) {
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()

        RewardedAd.load(
            activity,
            context.getString(R.string.ad_rewarded_id),
            adRequest,
            object : RewardedAdLoadCallback(){
                override fun onAdFailedToLoad(error: LoadAdError) {
                    super.onAdFailedToLoad(error)
                    callback(null)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    callback(rewardedAd)
                }
            }
        )

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
                        if (ifBack || userDailyStats.value.hardGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                            navHome()
                        } else {
                            setUpGame()
                        }

                    }
                }

            } else {
                Toast.makeText(context, "Te Salvaste del anuncio :c", Toast.LENGTH_SHORT).show()

                if (ifBack || userDailyStats.value.hardGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
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
            tryNumber = 0,
            intento1 = TryInfo(),
            intento2 = TryInfo(),
            intento3 = TryInfo(),
            intento4 = TryInfo(),
            intento5 = TryInfo(),
            isGameWon = null,
            secretWord = "",
            keyboard = keyboardCreator(),
            wordsTried = emptyList(),
            inputList = (1..6).map { null },
            indexFocused = 0,
            lettersHintsRemaining = 2,
            keyboardHintsRemaining = 1
        )
    }

}