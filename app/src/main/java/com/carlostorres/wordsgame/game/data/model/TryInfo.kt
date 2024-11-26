package com.carlostorres.wordsgame.game.data.model

import com.carlostorres.wordsgame.ui.components.word_line.WordCharState

data class TryInfo(
    val word : String = "",
    val resultado : List<Pair<String, WordCharState>> = emptyList()
)