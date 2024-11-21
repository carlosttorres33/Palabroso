package com.carlostorres.wordsgame.home.presentation

sealed class EasyEvents {

    data class OnInputTextChange(val inputText: String) : EasyEvents()
    object OnAcceptClick : EasyEvents()
    object OnDeleteClick : EasyEvents()

}