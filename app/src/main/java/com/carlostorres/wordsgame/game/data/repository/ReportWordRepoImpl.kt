package com.carlostorres.wordsgame.game.data.repository

import com.carlostorres.wordsgame.game.data.remote.model.ReportWordDto
import com.carlostorres.wordsgame.game.domain.repository.ReportWordRepository
import com.carlostorres.wordsgame.utils.Constants.REPORT_WORD_COLLECTION_PATH
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReportWordRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReportWordRepository {

    override suspend fun reportWord(
        wordLength: Int,
        wordId: String,
        reportedWord: ReportWordDto
    ) : Boolean {

        val reportWordRef =
            firestore.collection(wordLength.toString() + REPORT_WORD_COLLECTION_PATH)

        return try {

            reportWordRef
                .document(wordId)
                .set(reportedWord)
                .await()

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

}