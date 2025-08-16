package com.carlostorres.wordsgame.game.di

import android.content.Context
import androidx.room.Room
import com.carlostorres.wordsgame.game.data.local.WordGameDao
import com.carlostorres.wordsgame.game.data.local.WordGameDatabase
import com.carlostorres.wordsgame.game.data.local.LocalWordsDataSource
import com.carlostorres.wordsgame.game.data.local.StatsGameDao
import com.carlostorres.wordsgame.game.data.local.StatsGameDatabase
import com.carlostorres.wordsgame.game.data.remote.RemoteWordDataSource
import com.carlostorres.wordsgame.game.data.remote.WordApi
import com.carlostorres.wordsgame.game.data.repository.DataStoreOperationsImpl
import com.carlostorres.wordsgame.game.data.repository.ReportWordRepoImpl
import com.carlostorres.wordsgame.game.data.repository.StatsRepoImpl
import com.carlostorres.wordsgame.game.data.repository.WordsRepositoryImplementation
import com.carlostorres.wordsgame.game.domain.usecases.settings.CanAccessToAppUseCase
import com.carlostorres.wordsgame.game.domain.repository.DataStoreOperations
import com.carlostorres.wordsgame.game.domain.repository.ReportWordRepository
import com.carlostorres.wordsgame.game.domain.repository.StatsRepo
import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import com.carlostorres.wordsgame.game.domain.usecases.GameStatsUseCases
import com.carlostorres.wordsgame.game.domain.usecases.words.GetRandomWordUseCase
import com.carlostorres.wordsgame.game.domain.usecases.GameUseCases
import com.carlostorres.wordsgame.game.domain.usecases.MenuUseCases
import com.carlostorres.wordsgame.game.domain.usecases.stats.GetGameModeStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.OnboardingUseCases
import com.carlostorres.wordsgame.game.domain.usecases.StatsUseCases
import com.carlostorres.wordsgame.game.domain.usecases.coins.GetCoinsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.coins.UpdateCoinsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.easy.ClearEasyGameStateUseCase
import com.carlostorres.wordsgame.game.domain.usecases.easy.EasyGameStateUseCases
import com.carlostorres.wordsgame.game.domain.usecases.easy.ReadEasyGameStateUseCase
import com.carlostorres.wordsgame.game.domain.usecases.easy.SaveEasyGameStateUseCase
import com.carlostorres.wordsgame.game.domain.usecases.settings.ReadAccessToAppDataStore
import com.carlostorres.wordsgame.game.domain.usecases.stats.ReadDailyStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.settings.ReadInstructionsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.settings.SaveAccessToAppDataStore
import com.carlostorres.wordsgame.game.domain.usecases.settings.SaveInstructionsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.stats.GetAllStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.stats.UpdateDailyStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.stats.UpsertStatsUseCase
import com.carlostorres.wordsgame.game.domain.usecases.words.ReportWordUseCase
import com.carlostorres.wordsgame.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.utils.ConnectivityObserver
import com.carlostorres.wordsgame.utils.ConnectivityObserverImpl
import com.carlostorres.wordsgame.utils.Constants.BASE_URL_FIREBASE
import com.carlostorres.wordsgame.utils.Constants.REPORT_WORD_COLLECTION_PATH
import com.carlostorres.wordsgame.utils.GameSituations
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordsModule {

    @Provides
    @Singleton
    fun provideJsonSerializer(): Json {
        return Json {
            ignoreUnknownKeys = true  // Ignora campos JSON no mapeados
            isLenient = true         // Permite formatos JSON flexibles
            encodeDefaults = true    // Incluye valores por defecto
            // Configuración adicional si necesitas:
            prettyPrint = true       // Para logging legible (opcional)
            serializersModule = SerializersModule {
                // Registra explícitamente la clase sellada y sus subclases
                polymorphic(ButtonType::class) {
                    subclass(ButtonType.IsOnWord::class)
                    subclass(ButtonType.IsOnPosition::class)
                    subclass(ButtonType.IsNotInWord::class)
                    subclass(ButtonType.Unclicked::class)
                }
                polymorphic(WordCharState::class) {
                    subclass(WordCharState.Empty::class)
                    subclass(WordCharState.IsOnWord::class)
                    subclass(WordCharState.IsOnPosition::class)
                    subclass(WordCharState.IsNotInWord::class)
                }
                polymorphic(GameSituations::class) {
                    subclass(GameSituations.GameWon::class)
                    subclass(GameSituations.GameLost::class)
                    subclass(GameSituations.GameInProgress::class)
                    subclass(GameSituations.GameLoading::class)
                    subclass(GameSituations.GameError::class)
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideDataStoreOperations(
        @ApplicationContext context: Context,
        jsonSerializer: Json
    ) : DataStoreOperations {
        return DataStoreOperationsImpl(context, jsonSerializer)
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
    fun provideStatsGameDatabase(
        @ApplicationContext context: Context
    ) : StatsGameDatabase = Room.databaseBuilder(
        context = context,
        klass = StatsGameDatabase::class.java,
        name = "stats.db"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideStatsGameDao(
        database: StatsGameDatabase
    ) = database.statsGameDao()

    @Provides
    @Singleton
    fun provideLocalWordsDataSource(
        wordGameDao: WordGameDao,
        statsGameDao: StatsGameDao
    ) : LocalWordsDataSource = LocalWordsDataSource(wordGameDao = wordGameDao, statsGameDao = statsGameDao)

    @Provides
    @Singleton
    fun provideStatsRepo(
        localDataSource: LocalWordsDataSource
    ) : StatsRepo = StatsRepoImpl(localDataSource)

    @Singleton
    @Provides
    fun provideGameStatsUseCases(
        statsRepo: StatsRepo
    ) : GameStatsUseCases = GameStatsUseCases(
        upsertStatsUseCase = UpsertStatsUseCase(statsRepo),
        getGameModeStatsUseCase = GetGameModeStatsUseCase(statsRepo)
    )

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
        updateDailyStatsUseCase = UpdateDailyStatsUseCase(dataStoreOperations),
        readDailyStatsUseCase = ReadDailyStatsUseCase(dataStoreOperations),
        getCoinsUseCase = GetCoinsUseCase(dataStoreOperations),
        updateCoinsUseCase = UpdateCoinsUseCase(dataStoreOperations)
    )

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_FIREBASE)
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
    fun provideStatsUseCases(
        statsRepo: StatsRepo
    ) : StatsUseCases = StatsUseCases(
        getAllStatsUseCase = GetAllStatsUseCase(statsRepo)
    )

    @Singleton
    @Provides
    fun provideRemoteConfig() = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 30 })
        fetchAndActivate()
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext context: Context
    ) : ConnectivityObserver = ConnectivityObserverImpl(context)

    @Provides
    @Singleton
    fun provideMenuUseCases(
        dataStoreOperations: DataStoreOperations,
        repo: WordsRepository
    ) : MenuUseCases = MenuUseCases(
        canAccessToAppUseCase = CanAccessToAppUseCase(repo),
        saveAccessToAppUseCase = SaveAccessToAppDataStore(dataStoreOperations),
        readAccessToAppUseCase = ReadAccessToAppDataStore(dataStoreOperations)
    )

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideReportWordRepository(
        firestore: FirebaseFirestore
    ) : ReportWordRepository = ReportWordRepoImpl(firestore = firestore)

    @Provides
    @Singleton
    fun provideReportWordUseCase(
        repository: ReportWordRepository
    ) : ReportWordUseCase = ReportWordUseCase(repository)

    @Singleton
    @Provides
    fun provideEasyGameStateUseCases(
        dataStoreOperations: DataStoreOperations
    ) : EasyGameStateUseCases = EasyGameStateUseCases(
        saveEasyGameStateUseCase = SaveEasyGameStateUseCase(dataStoreOperations),
        readEasyGameStateUseCase = ReadEasyGameStateUseCase(dataStoreOperations),
        clearEasyGameStateUseCase = ClearEasyGameStateUseCase(dataStoreOperations)
    )

}
