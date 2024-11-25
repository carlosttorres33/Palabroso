package com.carlostorres.wordsgame.game.data.local

import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import javax.inject.Inject

class LocalWordsDataSource @Inject constructor(
    private val wordGameDao: WordGameDao
) {

    suspend fun getWords(): List<WordEntity> {
        return wordGameDao.getWords()
    }

    fun upsertWords() {

        val wordList = listOf(
            "Abuso",
            "Acoso",
            "Adios",
            "Alpes",
            "Arena",
            "Audio",
            "Bravo",
            "Cañon",
            "Cerca",
            "Chile",
            "Clase",
            "Crema",
            "Cruel",
            "Fuego",
            "Golpe",
            "Grupo",
            "Ideal",
            "Llave",
            "Metal",
            "Radio",
            "Antro",
            "Banda",
            "Chafa",
            "Chale",
            "Chido",
            "Crudo",
            "Facha",
            "Fonda",
            "Fresa",
            "Gacho",
            "Guapo",
            "Guero",
            "Jalar",
            "Lucas",
            "Padre",
            "Rollo"
        ).filter { it.trim().length == 5 }.map { WordEntity(word = it.uppercase()) }

        wordGameDao.upsertWords(wordList)
    }

}