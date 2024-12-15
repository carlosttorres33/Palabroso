package com.carlostorres.wordsgame.utils

sealed class ButtonPlace{
    object BottomEnd : ButtonPlace()
    object BottomStart : ButtonPlace()
    object Center : ButtonPlace()
    object Bottom : ButtonPlace()
}

enum class HintType{
    ONE_LETTER, KEYBOARD
}