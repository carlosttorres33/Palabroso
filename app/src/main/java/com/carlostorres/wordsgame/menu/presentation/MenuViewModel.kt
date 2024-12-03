package com.carlostorres.wordsgame.menu.presentation

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.domain.usecases.GameUseCases
import com.carlostorres.wordsgame.game.domain.usecases.MenuUseCases
import com.carlostorres.wordsgame.utils.ConnectionStatus
import com.carlostorres.wordsgame.utils.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
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

    private val _canAccessToApp = MutableStateFlow(true)
    val canAccessToApp: StateFlow<Boolean> = _canAccessToApp.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkUserVersion()
            useCases.readDailyStatsUseCase().collect { stats ->
                _dailyStats.value = stats
            }
            menuUseCases.readAccessToAppUseCase().collect {
                _canAccessToApp.value = it
            }
        }
    }

    fun updateDailyStats(stats: UserDailyStats) {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateDailyStatsUseCase(stats)
        }
    }

    private fun checkUserVersion() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = menuUseCases.canAccessToAppUseCase()
            Log.d("Version", result.toString())
            state = state.copy(
                blockVersion = !result
            )
            if (result != null) {
                menuUseCases.saveAccessToAppUseCase(!result)
            }
        }
    }

}