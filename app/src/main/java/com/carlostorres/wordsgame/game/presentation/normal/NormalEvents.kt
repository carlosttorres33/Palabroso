package com.carlostorres.wordsgame.game.presentation.normal

sealed class NormalEvents {

    data class OnInputTextChange(val inputText: String) : NormalEvents()
    object OnAcceptClick : NormalEvents()
    object OnDeleteClick : NormalEvents()

}