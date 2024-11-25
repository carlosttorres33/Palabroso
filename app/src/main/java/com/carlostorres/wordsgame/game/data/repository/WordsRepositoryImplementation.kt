package com.carlostorres.wordsgame.game.data.repository

import android.content.Context
import android.util.Log
import com.carlostorres.wordsgame.game.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import com.carlostorres.wordsgame.game.data.remote.RemoteWordDataSource
import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import com.carlostorres.wordsgame.utils.Constants.REMOTE_CONFIG_MIN_VERSION_KEY
import com.carlostorres.wordsgame.utils.InternetCheck
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WordsRepositoryImplementation @Inject constructor(
    private val context: Context,
    private val remoteConfig : FirebaseRemoteConfig,
    private val localDataSource: LocalWordsDataSource,
    private val remoteDataSource: RemoteWordDataSource
) : WordsRepository {

    override suspend fun getWords(): List<WordEntity> {
        return localDataSource.getWords()
    }

    override fun upsertWords() {
        return localDataSource.upsertWords()
    }

    override suspend fun getRandomWord(wordsTried : List<String>, wordLength: Int) : String{

        return if (InternetCheck.isNetworkAvailable()){
            var word = remoteDataSource.getRandomWord(length = wordLength)

            println(word)

            while (wordsTried.contains(word) || word.length != wordLength){
                word = remoteDataSource.getRandomWord(length = wordLength)
            }

            Log.d("SecretWord", word)
            word.trim().take(wordLength)

        } else {

            while (localDataSource.getWords().isEmpty()){
                localDataSource.upsertWords()
            }

            val words = localDataSource.getWords()

            val word = words.filter { !wordsTried.contains(it.word) }.random().word

            Log.d("SecretWord", word)
            word
        }

    }

    override suspend fun getOfflineRandomWord(wordsTried: List<String>): String {
        while (localDataSource.getWords().isEmpty()){
            localDataSource.upsertWords()
        }

        val words = localDataSource.getWords()


        return words.filter { !wordsTried.contains(it.word) }.random().word
    }

    override suspend fun getMinAllowedVersion(): List<Int> {

        remoteConfig.fetch(0)
        remoteConfig.activate().await()

        val minVersion = remoteConfig.getString(REMOTE_CONFIG_MIN_VERSION_KEY)

        return if (minVersion.isBlank()){
            return listOf(0,0,0)
        }else{
            minVersion.split(".").map { it.toInt() }
        }
    }

    override fun getCurrentVersion(): List<Int> {
        return try {

            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName.split(".").map { it.toInt() }

        }catch (e: Exception){
            Log.d("", e.message.toString())
            listOf(0,0,0)
        }
    }

}