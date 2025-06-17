package com.carlostorres.wordsgame.game.domain.usecases.words

import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import com.carlostorres.wordsgame.game.presentation.WordModel
import javax.inject.Inject

class GetRandomWordUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {
    //Production
    suspend operator fun invoke(wordsTried : List<String>, group : String, wordLength : Int, dayTries : Int, gameDifficult: String) : WordModel {
      return wordsRepository.getRandomWord(wordsTried, group, dayTries, wordLength, gameDifficult)
    }

    //Offline Tests
//    suspend operator fun invoke(wordsTried : List<String>, wordLength : Int) : String = wordsRepository.getOfflineRandomWord(wordsTried, wordLength)

}