package com.carlostorres.wordsgame.game.presentation

sealed class GameEvents {

    object OnAcceptClick : GameEvents()
    data class OnFocusChange(val index: Int) : GameEvents()
    data class OnKeyboardClick(val char: Char, val index: Int) : GameEvents()
    object OnKeyboardDeleteClick : GameEvents()

}