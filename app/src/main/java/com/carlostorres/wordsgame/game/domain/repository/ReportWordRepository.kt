package com.carlostorres.wordsgame.game.domain.repository

import com.carlostorres.wordsgame.game.data.remote.model.ReportWordDto
import com.carlostorres.wordsgame.utils.ViewState
import kotlinx.coroutines.flow.Flow

interface ReportWordRepository {

    suspend fun reportWord(
        wordLength : Int,
        wordId : String,
        reportedWord : ReportWordDto
    ) : Flow<ViewState<Boolean>>

}