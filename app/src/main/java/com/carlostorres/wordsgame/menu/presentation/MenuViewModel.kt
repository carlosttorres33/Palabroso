package com.carlostorres.wordsgame.menu.presentation

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
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.domain.usecases.GameUseCases
import com.carlostorres.wordsgame.game.domain.usecases.MenuUseCases
import com.carlostorres.wordsgame.utils.ConnectionStatus
import com.carlostorres.wordsgame.utils.ConnectivityObserver
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
class MenuViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val useCases: GameUseCases,
    private val menuUseCases: MenuUseCases,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    var state by mutableStateOf(MenuState())
        private set

    val isConnected = connectivityObserver.isConnected.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        ConnectionStatus.Available
    )

    private val _dailyStats = MutableStateFlow(
        UserDailyStats(
            easyGamesPlayed = 0,
            normalGamesPlayed = 0,
            hardGamesPlayed = 0,
            lastPlayedDate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        )
    )
    val dailyStats: StateFlow<UserDailyStats> = _dailyStats.asStateFlow()

    private val _canAccessToApp = MutableStateFlow(false)
    val canAccessToApp: StateFlow<Boolean> = _canAccessToApp.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkUserVersion()
            getDailyStats()
            canAccessToApp()
            getUserCoins()
        }
    }

    private fun getCoinsFromAd(actualUserCoins: Int) = viewModelScope.launch(Dispatchers.IO) {
        useCases.updateCoinsUseCase(actualUserCoins + 75)
    }

    fun updateDailyStats(stats: UserDailyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateDailyStatsUseCase(stats)
        }
    }

    //region InitFunctions
    private fun canAccessToApp() = viewModelScope.launch {
        menuUseCases.readAccessToAppUseCase().collectLatest { canAccess ->
            _canAccessToApp.value = canAccess
        }
    }

    private fun getDailyStats() = viewModelScope.launch {
        useCases.readDailyStatsUseCase().collectLatest { stats ->
            Log.d("MenuViewModel", stats.toString())
            _dailyStats.value = stats
        }
    }

    private fun getUserCoins() = viewModelScope.launch {
        useCases.getCoinsUseCase().collectLatest{ coins ->
            state = state.copy(
                userCoins = coins
            )
        }
    }

    private fun checkUserVersion() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = menuUseCases.canAccessToAppUseCase()
            Log.d("Version", result.toString())
            state = state.copy(
                blockVersion = !result
            )
            menuUseCases.saveAccessToAppUseCase(!result)
        }
    }
    //endregion

    //region Rewarded Ad
    fun showRewardedAd(activity: Activity, actualUserCoins: Int) {
        state = state.copy(isLoading = true)
        loadRewardedAd(activity){ rewardedAd ->

            if (rewardedAd != null){

                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback(){
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        getCoinsFromAd(actualUserCoins)
                        state = state.copy(isLoading = false)
                    }
                }

                rewardedAd.show(activity, OnUserEarnedRewardListener { rewardItem ->
                    val amount = rewardItem.amount
                })

            }else{
                Toast.makeText(context, "Por ahora no se puede mostrar el anuncio :c", Toast.LENGTH_SHORT).show()
                state = state.copy(isLoading = false)
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
    //endregion

    fun showCoinsDialog(show: Boolean){
        state = state.copy(
            showCoinsDialog = show
        )
    }

}