package com.carlostorres.wordsgame.game.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordGameDao {

    @Query("SELECT * FROM words_table WHERE length = :length ORDER BY RANDOM() LIMIT 1")
    fun getRandomWord(length: Int): WordEntity

}