package com.carlostorres.wordsgame.game.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.carlostorres.wordsgame.game.data.local.model.WordEntity

@Dao
interface WordGameDao {

    @Query("Select * from words_table")
    suspend fun getWords(): List<WordEntity>

    @Upsert
    fun upsertWords(words: List<WordEntity>)

    @Query("SELECT * FROM words_table WHERE length = :length ORDER BY RANDOM() LIMIT 1")
    fun getRandomWord(length: Int): WordEntity

}