package com.carlostorres.wordsgame.utils

import com.carlostorres.wordsgame.utils.Constants.KEYBOARD_HINT_PRICE
import com.carlostorres.wordsgame.utils.Constants.ONE_LETTER_HINT_PRICE

fun getHintCoast(hintType: HintType) : Int{
    return when(hintType){
        HintType.ONE_LETTER -> ONE_LETTER_HINT_PRICE
        HintType.KEYBOARD -> KEYBOARD_HINT_PRICE
    }
}
