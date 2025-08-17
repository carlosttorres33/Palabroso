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
import com.carlostorres.wordsgame.game.domain.usecases.state.normal.NormalGameStateUseCases
import com.carlostorres.wordsgame.game.presentation.GameEvents
import com.carlostorres.wordsgame.game.presentation.WordModel
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.utils.ConnectionStatus
import com.carlostorres.wordsgame.utils.ConnectivityObserver
import com.carlostorres.wordsgame.utils.Constants.EP_5_LETTERS
import com.carlostorres.wordsgame.utils.Constants.NORMAL_WORD_LENGTH
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.HintType
import com.carlostorres.wordsgame.utils.difficultToString
import com.carlostorres.wordsgame.utils.getHintCoast
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class NormalViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCases: GameUseCases,
    private val gameStatsUseCases: GameStatsUseCases,
    private val connectivityObserver: ConnectivityObserver,
    private val statsUseCases: NormalGameStateUseCases
) : ViewModel() {

    var state by mutableStateOf(NormalState())
        private set

    val isConnected = connectivityObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ConnectionStatus.Available
    )

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

    //val userCoins = useCases.getCoinsUseCase()

    init {
        viewModelScope.launch {
            getDailyStats()
            getUserCoins()
        }
    }

    private fun getDailyStats() = viewModelScope.launch(Dispatchers.IO) {
        useCases.readDailyStatsUseCase().collect { stats ->
            _userDailyStats.value = stats
        }
    }

    private fun updateDailyStats(win: Boolean, tryNumber : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            gameStatsUseCases.upsertStatsUseCase(
                StatsEntity(
                    wordGuessed = state.secretWord.word,
                    gameDifficult = difficultToString(GameDifficult.Normal),
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

                val lastState = statsUseCases.readNormalGameStateUseCase()

                    if (lastState == null){

                    val word = useCases.getRandomWordUseCase(
                        wordsTried = state.secretWordsList,
                        wordLength = NORMAL_WORD_LENGTH,
                        dayTries = userDailyStats.value.normalGamesPlayed,
                        group = EP_5_LETTERS,
                        gameDifficult = difficultToString(GameDifficult.Normal)
                    )

                    state = if (word.word.isNotEmpty()) {
                        state.copy(
                            secretWord = word,
                            gameSituation = GameSituations.GameInProgress,
                            secretWordsList = state.secretWordsList.plus(word.word)
                        )
                    } else {
                        state.copy(
                            gameSituation = GameSituations.GameError("Error Desconocido")
                        )
                    }

                }else{
                    state = lastState
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

    private fun onAcceptClick(actualUserCoins : Int) = viewModelScope.launch {

        state = state.copy(
            wordsTried = state.wordsTried.plus(state.inputList.joinToString(""))
        )

        val resultado = validateIfWordContainsLetter()

        if (state.inputList.joinToString("").uppercase() == state.secretWord.word.uppercase()) {
            state = state.copy(
                gameSituation = GameSituations.GameWon,
            )
            statsUseCases.clearNormalGameStateUseCase()
            increaseNormalGamesPlayed()
            getCoinsFromWin(actualUserCoins)
            updateDailyStats(true, state.tryNumber)
        } else if (state.tryNumber >= 4) {
            state = state.copy(
                gameSituation = GameSituations.GameLost,
            )
            statsUseCases.clearNormalGameStateUseCase()
            increaseNormalGamesPlayed()
            updateDailyStats(false, state.tryNumber)
        }

        Log.d(
            "secretWord",
            "${state.inputList.joinToString("").uppercase()} == ${state.secretWord.word.uppercase()}"
        )

        when (state.tryNumber) {
            0 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento1 = state.intento1.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..5).map { null },
                    indexFocused = 0
                )
                if (state.gameSituation is GameSituations.GameInProgress){
                    statsUseCases.saveNormalGameStateUseCase(state)
                }
            }

            1 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento2 = state.intento2.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..5).map { null },
                    indexFocused = 0
                )
                if (state.gameSituation is GameSituations.GameInProgress){
                    statsUseCases.saveNormalGameStateUseCase(state)
                }
            }

            2 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento3 = state.intento3.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..5).map { null },
                    indexFocused = 0
                )
                if (state.gameSituation is GameSituations.GameInProgress){
                    statsUseCases.saveNormalGameStateUseCase(state)
                }
            }

            3 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento4 = state.intento4.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..5).map { null },
                    indexFocused = 0
                )
                if (state.gameSituation is GameSituations.GameInProgress){
                    statsUseCases.saveNormalGameStateUseCase(state)
                }
            }

            4 -> {
                state = state.copy(
                    tryNumber = state.tryNumber + 1,
                    intento5 = state.intento5.copy(
                        word = state.inputList.joinToString(""),
                        resultado = resultado
                    ),
                    inputList = (1..5).map { null },
                    indexFocused = 0
                )
                if (state.gameSituation is GameSituations.GameInProgress){
                    statsUseCases.saveNormalGameStateUseCase(state)
                }
            }

            else -> {
                resetGame()
                statsUseCases.clearNormalGameStateUseCase()
            }
        }

    }


    private fun validateIfWordContainsLetter(): List<Pair<String, WordCharState>> {

        val resultado = mutableListOf<Pair<String, WordCharState>>()

        for (i in state.secretWord.word.indices) {
            if (state.secretWord.word[i].uppercase() == state.inputList[i]?.uppercase().orEmpty()) {
                resultado.add(Pair(state.inputList[i].toString(), WordCharState.IsOnPosition))
                state = state.copy(
                    keyboard = state.keyboard.map {
                        if (it.char.uppercase() == state.inputList[i]?.uppercase().orEmpty()) it.copy(type = ButtonType.IsOnPosition) else it
                    },
                    indexesGuessed = if (state.indexesGuessed.contains(i)) state.indexesGuessed else state.indexesGuessed.plus(i)
                )
            } else if (state.secretWord.word.uppercase()
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

    private fun getCoinsFromAd(actualUserCoins: Int) = viewModelScope.launch(Dispatchers.IO) {
        useCases.updateCoinsUseCase(actualUserCoins + 75)
    }

    private fun buyHint(actualUserCoins: Int, discount: Int) = viewModelScope.launch {
        useCases.updateCoinsUseCase(actualUserCoins - discount)
    }

    fun onEvent(event: GameEvents) {

        when (event) {
            is GameEvents.OnAcceptClick -> {
                onAcceptClick(event.actualUserCoins)
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

    private fun getPreviousFocusedIndex(): Int {
        return state.indexFocused.minus(1).coerceAtLeast(0) ?: 0
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
                if (ifBack || userDailyStats.value.normalGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                    navHome()
                } else {
                    setUpGame()
                }
                Log.d("Ad Error", "Ad is null")

            }
        }

    }

    fun disable4KeyboardLettersHint(actualUserCoins: Int) = viewModelScope.launch {

        //get random index from keyboard list that doesnt contains secret word chars
        val randomIndex = (0..2). map { counterIndex ->
            var possibleIndex = (0 until state.keyboard.size).random()
            while (state.secretWord.word.contains(state.keyboard[possibleIndex].char) || state.keyboard[possibleIndex].type == ButtonType.IsNotInWord){
                possibleIndex = (0 until state.keyboard.size).random()
            }
            possibleIndex
        }

        val discount = getHintCoast(HintType.KEYBOARD)

        buyHint(actualUserCoins, discount)

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

        statsUseCases.saveNormalGameStateUseCase(
            state.copy(
                showKeyboardHintDialog = false,
                userCoins = actualUserCoins - discount
            )
        )

    }

    fun getOneLetterWord(actualUserCoins: Int) = viewModelScope.launch {

        if (state.indexesGuessed.size == 5){
            Toast.makeText(context, "Parece que ya tienes todas las letras", Toast.LENGTH_SHORT).show()
            state = state.copy(
                lettersHintsRemaining = state.lettersHintsRemaining-1
            )
            return@launch
        }

        val discount = getHintCoast(HintType.ONE_LETTER)

        buyHint(actualUserCoins, discount)

        val indexesUnknowns = (0..4).mapNotNull { index ->
            if (state.indexesGuessed.contains(index)){
                null
            }else{
                index
            }
        }

        val indexToShow = indexesUnknowns.random()

        state = state.copy(
            inputList = state.inputList.mapIndexed { currentIndex, currentChar ->
                if (currentIndex == indexToShow){
                    state.secretWord.word[indexToShow]
                }else{
                    currentChar
                }
            },
            lettersHintsRemaining = state.lettersHintsRemaining - 1,
            indexesGuessed = state.indexesGuessed.plus(indexToShow)
        )

        state = state.copy(
            indexFocused = getNextFocusedIndex()
        )

        statsUseCases.saveNormalGameStateUseCase(
            state = state.copy(
                showLetterHintDialog = false,
                userCoins = actualUserCoins - discount
            )
        )

    }

    fun showRewardedAd(activity: Activity, actualUserCoins: Int) {
        state = state.copy(gameSituation = GameSituations.GameLoading)
        loadRewardedAd(activity){ rewardedAd ->

            if (rewardedAd != null){

                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback(){
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        getCoinsFromAd(actualUserCoins)
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

    private fun getCoinsFromWin(actualUserCoins: Int) = viewModelScope.launch(Dispatchers.IO) {
        useCases.updateCoinsUseCase(actualUserCoins + 25)
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
            secretWord = WordModel("", 0),
            keyboard = keyboardCreator(),
            wordsTried = emptyList(),
            inputList = (1..5).map { null },
            indexFocused = 0,
            lettersHintsRemaining = 1,
            keyboardHintsRemaining = 1,
            indexesGuessed = emptyList(),
            isAlreadyReported = false
        )
    }

    private fun getUserCoins() = viewModelScope.launch {
        useCases.getCoinsUseCase().collectLatest{ coins ->
            state = state.copy(
                userCoins = coins
            )
            statsUseCases.saveNormalGameStateUseCase(
                state
            )
        }
    }

    fun hintDialogHandler(hintType: HintType, show: Boolean){
        state = when(hintType){
            HintType.ONE_LETTER -> {
                state.copy(
                    showLetterHintDialog = show
                )
            }

            HintType.KEYBOARD -> {
                state.copy(
                    showKeyboardHintDialog = show
                )
            }
        }
    }

    fun showCoinsDialog(show: Boolean) {
        state = state.copy(
            showCoinsDialog = show
        )
    }

    fun reportedWordStatus(wasReported : Boolean){
        state = state.copy(
            isAlreadyReported = wasReported
        )
    }

    fun showReportWordDialog(show: Boolean) {
        state = state.copy(
            showReportWordDialog = show
        )
    }

}