package com.carlostorres.wordsgame.game.domain.repository

interface WordsRepository {

    suspend fun getRandomWord(wordsTried : List<String>, group : String, dayTries : Int, wordLength : Int, gameDifficult: String) : String?

    suspend fun getOfflineRandomWord(wordsTried : List<String>, length : Int) : String

    suspend fun getMinAllowedVersion() : List<Int>
    fun getCurrentVersion() : List<Int>

}