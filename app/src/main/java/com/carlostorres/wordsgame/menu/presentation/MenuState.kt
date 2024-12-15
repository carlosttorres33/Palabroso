package com.carlostorres.wordsgame.menu.presentation

data class MenuState(
    val blockVersion : Boolean = false,
    val seenInstructions : Boolean = false,
    val isLoading : Boolean = false,
    val showCoinsDialog : Boolean = false,
    val userCoins : Int = 0,
)
