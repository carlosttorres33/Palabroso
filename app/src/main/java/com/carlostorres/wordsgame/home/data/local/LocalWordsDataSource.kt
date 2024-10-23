package com.carlostorres.wordsgame.home.data.local

import com.carlostorres.wordsgame.home.data.local.model.WordEntity
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
            "Radio"
        ).filter { it.length == 5 }.map { WordEntity(word = it) }

        wordGameDao.upsertWords(wordList)
    }

}