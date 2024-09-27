package com.carlostorres.wordsgame.home.di

import android.content.Context
import androidx.room.Room
import com.carlostorres.wordsgame.home.data.WordGameDao
import com.carlostorres.wordsgame.home.data.WordGameDatabase
import com.carlostorres.wordsgame.home.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.home.data.repository.WordsRepositoryImplementation
import com.carlostorres.wordsgame.home.domain.repository.WordsRepository
import com.carlostorres.wordsgame.home.domain.usecases.GetAllWordsUseCase
import com.carlostorres.wordsgame.home.domain.usecases.GetRandomWordUseCase
import com.carlostorres.wordsgame.home.domain.usecases.HomeUseCases
import com.carlostorres.wordsgame.home.domain.usecases.UpsertAllWordsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordsModule {

    @Provides
    @Singleton
    fun provideWordsDatabase(
        @ApplicationContext context: Context
    ) : WordGameDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = WordGameDatabase::class.java,
            name = "words.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordsDao(
        database: WordGameDatabase
    ) : WordGameDao {
        return database.wordGameDao()
    }

    @Provides
    @Singleton
    fun provideLocalWordsDataSource(
        wordGameDao: WordGameDao
    ) : LocalWordsDataSource = LocalWordsDataSource(wordGameDao = wordGameDao)

    @Provides
    @Singleton
    fun provideWordsRepository(
        localDataSource : LocalWordsDataSource
    ) : WordsRepository = WordsRepositoryImplementation(localDataSource = localDataSource)

    @Provides
    @Singleton
    fun provideHomeUseCases(
        wordsRepository: WordsRepository
    ) : HomeUseCases = HomeUseCases(
        getAllWordsUseCase = GetAllWordsUseCase(wordsRepository = wordsRepository),
        upsertAllWordsUseCase = UpsertAllWordsUseCase(wordsRepository = wordsRepository),
        getRandomWordUseCase = GetRandomWordUseCase(wordsRepository = wordsRepository)
    )

}