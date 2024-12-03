package com.carlostorres.wordsgame.stats.presentation

import com.carlostorres.wordsgame.ui.components.GameDifficult

sealed interface OnStatsEvents {
    data class onStatClicked(val difficult: GameDifficult): OnStatsEvents
}