package com.carlostorres.wordsgame.game.presentation

import kotlinx.serialization.Serializable

@Serializable
data class WordModel(
    val word : String,
    val id : Int
)
