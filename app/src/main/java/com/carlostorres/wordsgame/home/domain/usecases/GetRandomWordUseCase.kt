package com.carlostorres.wordsgame.home.domain.usecases

import com.carlostorres.wordsgame.home.domain.repository.WordsRepository
import javax.inject.Inject

class GetRandomWordUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    //Production
//    suspend operator fun invoke(wordsTried : List<String>) : String = wordsRepository.getRandomWord(wordsTried)

    //Offline Tests
    suspend operator fun invoke(wordsTried : List<String>) : String = wordsRepository.getOfflineRandomWord(wordsTried)

}