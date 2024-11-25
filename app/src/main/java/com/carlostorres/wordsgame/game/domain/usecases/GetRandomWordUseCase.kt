package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import javax.inject.Inject

class GetRandomWordUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    //Production
    suspend operator fun invoke(wordsTried : List<String>, wordLength : Int) : String = wordsRepository.getRandomWord(wordsTried, wordLength)

    //Offline Tests
//    suspend operator fun invoke(wordsTried : List<String>) : String = wordsRepository.getOfflineRandomWord(wordsTried)

}