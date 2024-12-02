package com.carlostorres.wordsgame.stats.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.game.domain.usecases.StatsUseCases
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.utils.ViewState
import com.carlostorres.wordsgame.utils.difficultToString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsUseCases: StatsUseCases
): ViewModel() {

    var state by mutableStateOf(StatsState())
        private set

//    init {
//        getAllStats()
//    }

     fun getAllStats() = viewModelScope.launch(Dispatchers.IO){
        state = state.copy(
            isLoading = true
        )
        try {
            val stats = statsUseCases.getAllStatsUseCase()

            if (stats.isNotEmpty()){
                state = state.copy(
                    easyStats = stats.filter { it.gameDifficult == difficultToString( GameDifficult.Easy) },
                    normalStats = stats.filter { it.gameDifficult == difficultToString( GameDifficult.Normal) },
                    hardStats = stats.filter { it.gameDifficult == difficultToString( GameDifficult.Hard) },
                    isLoading = false
                )
            }else{
                state = state.copy(
                    isLoading = false,
                    isError = "Aun no hay Datos por ver"
                )
            }

        }catch (e: Exception){
            state = state.copy(
                isLoading = false,
                isError = "Error al obtener estadisticas"
            )
            Log.e("StatsViewModel", "getAllStats: ${e.message}")
        }
    }

    fun onEvent(event: OnStatsEvents) {
        when(event){
            is OnStatsEvents.onStatClicked -> {
                state = when(event.difficult){
                    GameDifficult.Easy -> state.copy(statsShowed = GameDifficult.Easy)
                    GameDifficult.Hard -> state.copy(statsShowed = GameDifficult.Hard)
                    GameDifficult.Normal -> state.copy(statsShowed = GameDifficult.Normal)
                }
            }
        }
    }

}