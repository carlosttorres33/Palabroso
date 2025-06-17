package com.carlostorres.wordsgame.game.domain.usecases.words

import com.carlostorres.wordsgame.game.data.remote.model.ReportWordDto
import com.carlostorres.wordsgame.game.domain.repository.ReportWordRepository
import com.carlostorres.wordsgame.utils.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReportWordUseCase(
    private val repository: ReportWordRepository
) {
    operator fun invoke(
        wordLength: Int,
        wordId: String,
        reportedWord: ReportWordDto
    ): Flow<ViewState<Boolean>> = flow {

        emit(ViewState.Loading)

        try {

            val reportWordRef =
                repository.reportWord(
                    wordLength = wordLength,
                    wordId = wordId,
                    reportedWord = reportedWord
                )

            emit(ViewState.Success(reportWordRef))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(ViewState.Error(e.message.toString()))
        }

    }
}