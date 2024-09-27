package com.carlostorres.wordsgame.home.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.carlostorres.wordsgame.home.data.local.model.WordEntity

@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class WordGameDatabase : RoomDatabase() {

    abstract fun wordGameDao(): WordGameDao

}