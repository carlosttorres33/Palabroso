package com.carlostorres.wordsgame.home.domain.usecases

import com.carlostorres.wordsgame.home.domain.repository.WordsRepository
import javax.inject.Inject

class GetRandomWordUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {

    suspend operator fun invoke(wordsTried : List<String>) : String = wordsRepository.getRandomWord(wordsTried)

}