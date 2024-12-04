package com.carlostorres.wordsgame.game.data.remote

import retrofit2.Response
import javax.inject.Inject

class RemoteWordDataSource @Inject constructor(
    private val wordApi: WordApi
) {

    suspend fun getRandomWord(group: String, id: String): String? {
        val response = wordApi.getWord(group, id)
        return if (response.isSuccessful) {
            response.body()
        }else{
            null
        }
    }

}