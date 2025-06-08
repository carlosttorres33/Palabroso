package com.carlostorres.wordsgame.game.data.repository

import com.carlostorres.wordsgame.game.data.remote.model.ReportWordDto
import com.carlostorres.wordsgame.game.domain.repository.ReportWordRepository
import com.carlostorres.wordsgame.utils.Constants.REPORT_WORD_COLLECTION_PATH
import com.carlostorres.wordsgame.utils.ViewState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReportWordRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReportWordRepository {

    override suspend fun reportWord(
        wordLength: Int,
        wordId: String,
        reportedWord: ReportWordDto
    ) : Flow<ViewState<Boolean>> = flow {

        emit(ViewState.Loading)

        val reportWordRef =
            firestore.collection(wordLength.toString() + REPORT_WORD_COLLECTION_PATH)

        try {

            reportWordRef
                .document(wordId)
                .set(reportedWord)
                .await()

            emit(ViewState.Success(true))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(ViewState.Error(e.message.toString()))
        }

    }

}