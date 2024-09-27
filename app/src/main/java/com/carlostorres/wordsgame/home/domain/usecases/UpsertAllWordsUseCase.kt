package com.carlostorres.wordsgame.home.domain.usecases

import com.carlostorres.wordsgame.home.data.local.model.WordEntity
import com.carlostorres.wordsgame.home.domain.repository.WordsRepository
import javax.inject.Inject

class UpsertAllWordsUseCase @Inject constructor(
    private val wordsRepository: WordsRepository
) {

    operator fun invoke() = wordsRepository.upsertWords()

}