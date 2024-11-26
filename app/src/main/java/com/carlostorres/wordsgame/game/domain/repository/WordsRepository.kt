package com.carlostorres.wordsgame.game.domain.repository

import com.carlostorres.wordsgame.game.data.local.model.WordEntity

interface WordsRepository {

    suspend fun getWords(): List<WordEntity>

    fun upsertWords()

    suspend fun getRandomWord(wordsTried : List<String>, wordLength : Int) : String

    suspend fun getOfflineRandomWord(wordsTried : List<String>, length : Int) : String

    suspend fun getMinAllowedVersion() : List<Int>
    fun getCurrentVersion() : List<Int>

}