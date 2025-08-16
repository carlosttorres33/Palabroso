package com.carlostorres.wordsgame.game.data.model

import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import kotlinx.serialization.Serializable

@Serializable
data class TryInfo(
    val word : String = "",
    val resultado : List<Pair<String, WordCharState>> = emptyList()
)