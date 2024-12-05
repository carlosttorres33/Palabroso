package com.carlostorres.wordsgame.game.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlostorres.wordsgame.game.data.local.model.StatsEntity

@Database(entities = [StatsEntity::class], version = 2, exportSchema = false)
abstract class StatsGameDatabase : RoomDatabase() {

    abstract fun statsGameDao(): StatsGameDao

}