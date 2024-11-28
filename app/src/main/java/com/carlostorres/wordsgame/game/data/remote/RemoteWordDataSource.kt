package com.carlostorres.wordsgame.game.data.remote

import javax.inject.Inject

class RemoteWordDataSource @Inject constructor(
    private val wordApi: WordApi
) {

    suspend fun getRandomWord(language: String = "es",count: Int = 1, length: Int): String {
        return wordApi.getRandomWord(language = language, numberOfWords = count, wordLength = length)[0]
    }

}