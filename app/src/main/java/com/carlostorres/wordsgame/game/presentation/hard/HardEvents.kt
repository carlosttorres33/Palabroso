package com.carlostorres.wordsgame.game.presentation.hard

sealed class HardEvents {

    data class OnInputTextChange(val inputText: String) : HardEvents()
    object OnAcceptClick : HardEvents()
    object OnDeleteClick : HardEvents()

}