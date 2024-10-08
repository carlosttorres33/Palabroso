package com.carlostorres.wordsgame.home.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WordApi {

    @GET("public-random-word")
    suspend fun getRandomWord(
        @Query("c") count : Int,
        @Query("l") length : Int
    ): Array<String>

}