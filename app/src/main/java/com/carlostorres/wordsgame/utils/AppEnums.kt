package com.carlostorres.wordsgame.utils

sealed class ButtonPlace{
    object BottomEnd : ButtonPlace()
    object BottomStart : ButtonPlace()
    object Center : ButtonPlace()
}

enum class HintType{
    ONE_LETTER, KEYBOARD
}