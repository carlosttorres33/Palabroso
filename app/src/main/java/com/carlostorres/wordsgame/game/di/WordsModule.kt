package com.carlostorres.wordsgame.game.di

import android.content.Context
import androidx.room.Room
import com.carlostorres.wordsgame.game.data.local.WordGameDao
import com.carlostorres.wordsgame.game.data.local.WordGameDatabase
import com.carlostorres.wordsgame.game.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.game.data.remote.RemoteWordDataSource
import com.carlostorres.wordsgame.game.data.remote.WordApi
import com.carlostorres.wordsgame.game.data.repository.DataStoreOperationsImpl
import com.carlostorres.wordsgame.game.data.repository.WordsRepositoryImplementation
import com.carlostorres.wordsgame.game.domain.usecases.CanAccessToAppUseCase
import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import com.carlostorres.wordsgame.game.domain.usecases.GetRandomWordUseCase
import com.carlostorres.wordsgame.game.domain.usecases.GameUseCases
import com.carlostorres.wordsgame.game.domain.usecases.OnboardingUseCases
import com.carlostorres.wordsgame.game.domain.usecases.ReadDailyStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.ReadInstructionsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.SaveInstructionsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.UpdateDailyStatsUseCase
import com.carlostorres.wordsgame.utils.Constants.BASE_URL
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordsModule {

    @Provides
    @Singleton
    fun provideDataStoreOperations(
        @ApplicationContext context: Context
    ) : DataStoreOperations {
        return DataStoreOperationsImpl(context)
    }

    @Provides
    @Singleton
    fun provideWordsDatabase(
        @ApplicationContext context: Context
    ) : WordGameDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = WordGameDatabase::class.java,
            name = "words.db"
        ).createFromAsset("database/words.db")
            .fallbackToDestructiveMigration()
            .build()
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
    fun provideOnboardingUseCases(
        dataStoreOperations: DataStoreOperations
    ) : OnboardingUseCases = OnboardingUseCases(
        saveInstructionsUseCase = SaveInstructionsUseCase(dataStoreOperations),
        readInstructionsUseCase = ReadInstructionsUseCase(dataStoreOperations)
    )

    @Provides
    @Singleton
    fun provideHomeUseCases(
        wordsRepository: WordsRepository,
        dataStoreOperations: DataStoreOperations
    ) : GameUseCases = GameUseCases(
        getRandomWordUseCase = GetRandomWordUseCase(wordsRepository),
        canAccessToAppUseCase = CanAccessToAppUseCase(wordsRepository),
        updateDailyStatsUseCase = UpdateDailyStatsUseCase(dataStoreOperations),
        readDailyStatsUseCase = ReadDailyStatsUseCase(dataStoreOperations)
    )

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWordApi(retrofit: Retrofit): WordApi {
        return retrofit.create(WordApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteWordDataSource(
        wordApi: WordApi
    ) : RemoteWordDataSource = RemoteWordDataSource(wordApi = wordApi)

    @Provides
    @Singleton
    fun provideWordsRepository(
        @ApplicationContext context: Context,
        localDataSource : LocalWordsDataSource,
        remoteDataSource: RemoteWordDataSource,
        remoteConfig: FirebaseRemoteConfig
    ) : WordsRepository = WordsRepositoryImplementation(
        context = context,
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        remoteConfig = remoteConfig
    )

    @Singleton
    @Provides
    fun provideRemoteConfig() = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 30 })
        fetchAndActivate()
    }

}
