package com.carlostorres.wordsgame.game.domain.repository

import com.carlostorres.wordsgame.game.data.remote.model.ReportWordDto

interface ReportWordRepository {

    suspend fun reportWord(
        wordLength : Int,
        wordId : String,
        reportedWord : ReportWordDto
    ) : Boolean

}