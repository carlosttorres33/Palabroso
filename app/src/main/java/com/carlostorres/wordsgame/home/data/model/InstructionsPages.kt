package com.carlostorres.wordsgame.home.data.model

import androidx.annotation.DrawableRes
import com.carlostorres.wordsgame.R

sealed class InstructionsPages(
    @DrawableRes val image: Int? = null,
    val title: String,
) {

    object First : InstructionsPages(
        title = "Cómo Jugar"
    )
    object Second : InstructionsPages(
        image = R.drawable.example1,
        title = "Ejemplo"
    )
    object Third : InstructionsPages(
        image = R.drawable.example2,
        title = "Consejos para ganar!!"
    )

}