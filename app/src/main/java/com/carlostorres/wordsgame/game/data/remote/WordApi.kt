package com.carlostorres.wordsgame.game.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordApi {

    //1st API
//    @GET("public-random-word")
//    suspend fun getRandomWord(
//        @Query("c") count : Int,
//        @Query("l") length : Int
//    ): Array<String>

    @GET("palabras/{grupo}/{id}.json")
    suspend fun getWord(
        @Path("grupo") grupo: String,
        @Path("id") id: String
    ): Response<String?>

}