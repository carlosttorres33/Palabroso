package com.carlostorres.wordsgame.game.domain.repository

import com.carlostorres.wordsgame.game.presentation.WordModel

interface WordsRepository {

    suspend fun getRandomWord(wordsTried : List<String>, group : String, dayTries : Int, wordLength : Int, gameDifficult: String) : WordModel

    suspend fun getOfflineRandomWord(wordsTried : List<String>, length : Int) : String

    suspend fun getMinAllowedVersion() : List<Int>
    fun getCurrentVersion() : List<Int>

}