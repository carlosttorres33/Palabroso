package com.carlostorres.wordsgame.game.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlostorres.wordsgame.game.data.local.model.WordEntity

@Database(entities = [WordEntity::class], version = 4, exportSchema = false)
abstract class WordGameDatabase : RoomDatabase() {

    abstract fun wordGameDao(): WordGameDao

}