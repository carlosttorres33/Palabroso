package com.carlostorres.wordsgame.utils

import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import com.carlostorres.wordsgame.ui.components.keyboard.KeyboardChar
import java.text.Normalizer
import java.util.regex.Pattern

fun keyboardCreator() : List<KeyboardChar> {
    return listOf(
        KeyboardChar("Q"),
        KeyboardChar("W"),
        KeyboardChar("E"),
        KeyboardChar("R"),
        KeyboardChar("T"),
        KeyboardChar("Y"),
        KeyboardChar("U"),
        KeyboardChar("I"),
        KeyboardChar("O"),
        KeyboardChar("P"),
        KeyboardChar("A"),
        KeyboardChar("S"),
        KeyboardChar("D"),
        KeyboardChar("F"),
        KeyboardChar("G"),
        KeyboardChar("H"),
        KeyboardChar("J"),
        KeyboardChar("K"),
        KeyboardChar("L"),
        KeyboardChar("Ñ"),
        KeyboardChar("Z"),
        KeyboardChar("X"),
        KeyboardChar("C"),
        KeyboardChar("V"),
        KeyboardChar("B"),
        KeyboardChar("N"),
        KeyboardChar("M")
    )
}

fun String.toWordEntity() : WordEntity = WordEntity(
    word = this.uppercase().trim(),
    length = this.length
)

fun removeAccents(input: String): String {
    val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(normalized).replaceAll("")
}