package com.carlostorres.wordsgame.game.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.carlostorres.wordsgame.game.data.local.model.StatsEntity
import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import com.carlostorres.wordsgame.utils.toWordEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [WordEntity::class], version = 4, exportSchema = false)
abstract class WordGameDatabase : RoomDatabase() {

    abstract fun wordGameDao(): WordGameDao

}