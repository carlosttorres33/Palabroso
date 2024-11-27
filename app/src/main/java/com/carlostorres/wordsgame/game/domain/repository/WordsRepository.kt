package com.carlostorres.wordsgame.game.domain.repository

interface WordsRepository {

    suspend fun getRandomWord(wordsTried : List<String>, wordLength : Int, dayTries : Int) : String

    suspend fun getOfflineRandomWord(wordsTried : List<String>, length : Int) : String

    suspend fun getMinAllowedVersion() : List<Int>
    fun getCurrentVersion() : List<Int>

}