package com.carlostorres.wordsgame.game.data.repository

import android.content.Context
import android.health.connect.datatypes.units.Length
import android.util.Log
import com.carlostorres.wordsgame.game.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import com.carlostorres.wordsgame.game.data.remote.RemoteWordDataSource
import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import com.carlostorres.wordsgame.utils.Constants.REMOTE_CONFIG_MIN_VERSION_KEY
import com.carlostorres.wordsgame.utils.InternetCheck
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WordsRepositoryImplementation @Inject constructor(
    private val context: Context,
    private val remoteConfig: FirebaseRemoteConfig,
    private val localDataSource: LocalWordsDataSource,
    private val remoteDataSource: RemoteWordDataSource
) : WordsRepository {

    override suspend fun getRandomWord(
        wordsTried: List<String>,
        wordLength: Int,
        dayTries: Int
    ): String {

        return if (InternetCheck.isNetworkAvailable()) {

            var word = if (dayTries < NUMBER_OF_GAMES_ALLOWED-1) {
                Log.d("Repo", "Internet is available and first $NUMBER_OF_GAMES_ALLOWED tries")
                remoteDataSource.getRandomWord(length = wordLength)
            }else{
                Log.d("Repo", "Internet is available but $NUMBER_OF_GAMES_ALLOWED try so we get offline word")
                getOfflineRandomWord(wordsTried = wordsTried, length = wordLength)
            }

            println(word)

            while (wordsTried.contains(word) || word.length != wordLength) {
                word = if (dayTries < NUMBER_OF_GAMES_ALLOWED-1) {
                    getOfflineRandomWord(wordsTried = wordsTried, length = wordLength)
                } else {
                    remoteDataSource.getRandomWord(length = wordLength)
                }
            }

            Log.d("SecretWord", word)
            word.trim().take(wordLength)

        } else {

            val word = getOfflineRandomWord(wordsTried = wordsTried, length = wordLength)

            Log.d("SecretWord", word)
            word
        }

    }

    override suspend fun getOfflineRandomWord(wordsTried: List<String>, length: Int): String {

        var word = localDataSource.getRandomWord(length = length)

        while (wordsTried.contains(word) || word.length != length) {
            word = localDataSource.getRandomWord(length = length)
        }

        return word.uppercase()

    }

    override suspend fun getMinAllowedVersion(): List<Int> {

        remoteConfig.fetch(0)
        remoteConfig.activate().await()

        val minVersion = remoteConfig.getString(REMOTE_CONFIG_MIN_VERSION_KEY)

        return if (minVersion.isBlank()) {
            return listOf(0, 0, 0)
        } else {
            minVersion.split(".").map { it.toInt() }
        }
    }

    override fun getCurrentVersion(): List<Int> {
        return try {

            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.split(".").map { it.toInt() }

        } catch (e: Exception) {
            Log.d("", e.message.toString())
            listOf(0, 0, 0)
        }
    }

}