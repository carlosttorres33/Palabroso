package com.carlostorres.wordsgame.home.data.remote

import javax.inject.Inject

class RemoteWordDataSource @Inject constructor(
    private val wordApi: WordApi
) {

    suspend fun getRandomWord(count: Int = 1, length: Int = 5): String {
        return wordApi.getRandomWord(count, length)[0]
    }

}