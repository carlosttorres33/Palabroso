package com.carlostorres.wordsgame.game.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsGameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStats(stat: StatsEntity)

    @Query("SELECT COUNT(*) FROM stats_table WHERE gameDifficult = :gameDifficult AND win = :win")
    fun getGameModeStats(gameDifficult: String, win: Boolean): Flow<Int>

    @Query("SELECT * FROM stats_table")
    fun getAllStats(): List<StatsEntity>

}