package com.carlostorres.wordsgame.stats.presentation

import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.ui.components.GameDifficult

data class StatsState(
    val easyStats : List<StatsEntity> = emptyList(),
    val normalStats : List<StatsEntity> = emptyList(),
    val hardStats : List<StatsEntity> = emptyList(),
    val isLoading : Boolean = true,
    val isError : String? = null,
    val statsShowed : GameDifficult = GameDifficult.Easy
)

