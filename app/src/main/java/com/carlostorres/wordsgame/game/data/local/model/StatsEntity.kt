package com.carlostorres.wordsgame.game.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stats_table")
data class StatsEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val wordGuessed : String,
    val gameDifficult : String,
    val win : Boolean,
    val attempts : Int
)

/**
 * word = chile
 * gameDifficult = normal
 * win = true
 * attempts = 3
 *
 * select * from stats_table where gameMode = normal and win = true
 * select * from stats_table where gameMode = normal and win = false
 */