package com.carlostorres.wordsgame.game.presentation.easy

sealed class EasyEvents {

    data class OnInputTextChange(val inputText: String) : EasyEvents()
    object OnAcceptClick : EasyEvents()
    object OnDeleteClick : EasyEvents()

}