package com.carlostorres.wordsgame.home.domain.repository

import com.carlostorres.wordsgame.home.data.local.model.WordEntity

interface WordsRepository {

    suspend fun getWords(): List<WordEntity>

    fun upsertWords()

    suspend fun getRandomWord(wordsTried : List<String>) : String
    suspend fun getOfflineRandomWord(wordsTried : List<String>) : String

    suspend fun getMinAllowedVersion() : List<Int>
    fun getCurrentVersion() : List<Int>

}