package com.carlostorres.wordsgame.game.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.carlostorres.wordsgame.game.data.local.model.WordEntity

@Dao
interface WordGameDao {

    @Query("Select * from WordEntity")
    suspend fun getWords(): List<WordEntity>

    @Upsert
    fun upsertWords(words: List<WordEntity>)

}