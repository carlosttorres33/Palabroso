package com.carlostorres.wordsgame.game.domain.usecases

import com.carlostorres.wordsgame.game.domain.repository.WordsRepository
import javax.inject.Inject

class UpsertAllWordsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {

    operator fun invoke() = wordsRepository.upsertWords()

}