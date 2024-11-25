package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.data.local.model.WordEntity
import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import javax.inject.Inject

class GetAllWordsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {

    suspend operator fun invoke() :List<WordEntity> = wordsRepository.getWords()

}