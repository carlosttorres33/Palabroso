package com.carlostorres.wordsgame.home.data.repository

import android.content.Context
import android.util.Log
import com.carlostorres.wordsgame.home.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.home.data.local.model.WordEntity
import com.carlostorres.wordsgame.home.data.remote.RemoteWordDataSource
import com.carlostorres.wordsgame.home.domain.repository.WordsRepository
import com.carlostorres.wordsgame.utils.Constants.REMOTE_CONFIG_MIN_VERSION_KEY
import com.carlostorres.wordsgame.utils.InternetCheck
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.log

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

    override suspend fun getRandomWord(wordsTried : List<String>) : String{

        return if (InternetCheck.isNetworkAvailable()){
            var word = remoteDataSource.getRandomWord()

            println(word)

            while (wordsTried.contains(word)){
                word = remoteDataSource.getRandomWord()
            }

            word.trim().take(5)
        } else {
            while (localDataSource.getWords().isEmpty()){
                localDataSource.upsertWords()
            }

            val words = localDataSource.getWords()


            words.filter { !wordsTried.contains(it.word) }.random().word
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