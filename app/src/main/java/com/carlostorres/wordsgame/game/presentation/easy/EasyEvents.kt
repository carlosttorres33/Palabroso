package com.carlostorres.wordsgame.game.presentation.easy

sealed class EasyEvents {

    object OnAcceptClick : EasyEvents()
    data class OnFocusChange(val index: Int) : EasyEvents()
    data class OnKeyboardClick(val char: Char, val index: Int) : EasyEvents()
    object OnKeyboardDeleteClick : EasyEvents()

}