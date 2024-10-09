package com.carlostorres.wordsgame.home.data.repository

import com.carlostorres.wordsgame.home.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.home.data.local.model.WordEntity
import com.carlostorres.wordsgame.home.data.remote.RemoteWordDataSource
import com.carlostorres.wordsgame.home.domain.repository.WordsRepository
import javax.inject.Inject

class WordsRepositoryImplementation @Inject constructor(
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

        var word = remoteDataSource.getRandomWord()

        println(word)

        while (wordsTried.contains(word)){
            word = remoteDataSource.getRandomWord()
        }

        return word.trim().take(5)

    }

    override suspend fun getOfflineRandomWord(wordsTried: List<String>): String {
        while (localDataSource.getWords().isEmpty()){
            localDataSource.upsertWords()
        }

        val words = localDataSource.getWords()


        return words.filter { !wordsTried.contains(it.word) }.random().word
    }

}