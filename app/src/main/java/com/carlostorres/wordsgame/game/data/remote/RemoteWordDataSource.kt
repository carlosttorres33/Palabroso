package com.carlostorres.wordsgame.game.data.remote

import javax.inject.Inject

class RemoteWordDataSource @Inject constructor(
    private val wordApi: WordApi
) {

    suspend fun getRandomWord(count: Int = 1, length: Int): String {
        return wordApi.getRandomWord(count, length)[0]
    }

}