package com.carlostorres.wordsgame.menu.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.domain.usecases.GameUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val useCases: GameUseCases
) : ViewModel() {

    var state by mutableStateOf(MenuState())
        private set

    val dailyStats: Flow<UserDailyStats> = useCases.readDailyStatsUseCase()

    init {
        checkUserVersion()
    }

    fun updateDailyStats(stats: UserDailyStats){
        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateDailyStatsUseCase(stats)
        }
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

}