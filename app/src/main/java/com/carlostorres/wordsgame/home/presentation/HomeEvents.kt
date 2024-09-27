package com.carlostorres.wordsgame.home.presentation


sealed class HomeEvents {

    data class OnInputTextChange(val inputText: String) : HomeEvents()
    object OnAcceptClick : HomeEvents()
    object OnDeleteClick : HomeEvents()

}