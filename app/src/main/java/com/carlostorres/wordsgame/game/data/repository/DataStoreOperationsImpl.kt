package com.carlostorres.wordsgame.game.data.repository

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.carlostorres.wordsgame.game.data.repository.DataStoreOperationsImpl.PreferencesKeys.instructionsKey
import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.utils.Constants.EASY_GAMES_PLAYED_KEY
import com.carlostorres.wordsgame.utils.Constants.HARD_GAMES_PLAYED_KEY
import com.carlostorres.wordsgame.utils.Constants.INSTRUCTIONS_KEY
import com.carlostorres.wordsgame.utils.Constants.LAST_PLAYED_DATE_KEY
import com.carlostorres.wordsgame.utils.Constants.NORMAL_GAMES_PLAYED_KEY
import com.carlostorres.wordsgame.utils.Constants.PREFERENCES_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.text.SimpleDateFormat
import javax.inject.Inject

data class UserDailyStats(
    val easyGamesPlayed: Int,
    val normalGamesPlayed: Int,
    val hardGamesPlayed: Int,
    val lastPlayedDate: String
)

val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreOperationsImpl @Inject constructor(
    context: Context
) : DataStoreOperations {

    private object PreferencesKeys {
        val instructionsKey = booleanPreferencesKey(name = INSTRUCTIONS_KEY)

        val easyGamesPlayedKey = intPreferencesKey(name = EASY_GAMES_PLAYED_KEY)
        val normalGamesPlayedKey = intPreferencesKey(name = NORMAL_GAMES_PLAYED_KEY)
        val hardGamesPlayedKey = intPreferencesKey(name = HARD_GAMES_PLAYED_KEY)
        val lastPlayedDateKey = stringPreferencesKey(name = LAST_PLAYED_DATE_KEY)
    }

    private val dataStore = context.dataStore

    override suspend fun saveDailyStats(stats: UserDailyStats) {
        dataStore.edit {
            it[PreferencesKeys.easyGamesPlayedKey] = stats.easyGamesPlayed
            it[PreferencesKeys.normalGamesPlayedKey] = stats.normalGamesPlayed
            it[PreferencesKeys.hardGamesPlayedKey] = stats.hardGamesPlayed
            it[PreferencesKeys.lastPlayedDateKey] = stats.lastPlayedDate
        }
    }


    override fun readDailyStats(): Flow<UserDailyStats> = dataStore.data
        .catch {
            if (it is IOException){
                it.printStackTrace()
                emit(emptyPreferences())
            }else{
                throw it
            }
        }.map { preferences ->
            val easyGamesPlayed = preferences[PreferencesKeys.easyGamesPlayedKey] ?: 0
            val normalGamesPlayed = preferences[PreferencesKeys.normalGamesPlayedKey] ?: 0
            val hardGamesPlayed = preferences[PreferencesKeys.hardGamesPlayedKey] ?: 0
            val lastPlayedDate = preferences[PreferencesKeys.lastPlayedDateKey] ?: SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
            UserDailyStats(
                easyGamesPlayed = easyGamesPlayed,
                normalGamesPlayed = normalGamesPlayed,
                hardGamesPlayed = hardGamesPlayed,
                lastPlayedDate = lastPlayedDate
            )
        }

    override suspend fun saveInstructionsState(seen: Boolean) {
        dataStore.edit { preferences ->
            preferences[instructionsKey] = seen
        }
    }

    override fun readInstructionsState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }
            .map { preferences->
                val onBoardingState = preferences[instructionsKey] ?: false
                Log.d("Instructions", onBoardingState.toString())
                onBoardingState
            }
    }


}