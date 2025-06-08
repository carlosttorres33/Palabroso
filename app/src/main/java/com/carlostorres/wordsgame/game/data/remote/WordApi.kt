package com.carlostorres.wordsgame.game.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WordApi {

    @GET("palabras/{grupo}/{id}.json")
    suspend fun getWord(
        @Path("grupo") grupo: String,
        @Path("id") id: String
    ): Response<String?>

}