package com.carlostorres.wordsgame.game.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.carlostorres.wordsgame.game.data.repository.DataStoreOperationsImpl.PreferencesKeys.instructionsKey
import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.utils.Constants.INSTRUCTIONS_KEY
import com.carlostorres.wordsgame.utils.Constants.PREFERENCES_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreOperationsImpl @Inject constructor(
    context: Context
) : DataStoreOperations {

    private object PreferencesKeys {
        val instructionsKey = booleanPreferencesKey(name = INSTRUCTIONS_KEY)
    }

    private val dataStore = context.dataStore

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
                onBoardingState
            }
    }


}