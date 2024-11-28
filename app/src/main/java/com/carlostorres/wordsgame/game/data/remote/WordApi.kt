package com.carlostorres.wordsgame.game.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WordApi {

    //1st API
//    @GET("public-random-word")
//    suspend fun getRandomWord(
//        @Query("c") count : Int,
//        @Query("l") length : Int
//    ): Array<String>

    @GET("word")
    suspend fun getRandomWord(
        @Query("lang") language : String,
        @Query("number") numberOfWords : Int,
        @Query("length") wordLength : Int,
    ): Array<String>

}