package com.carlostorres.wordsgame.ui.components.dialogs.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.game.data.remote.model.ReportWordDto
import com.carlostorres.wordsgame.game.domain.usecases.words.ReportWordUseCase
import com.carlostorres.wordsgame.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportWordViewModel @Inject constructor(
    private val reportWordUseCase: ReportWordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<Boolean>>(ViewState.IDLE)
    val state = _state.asStateFlow()

    fun reportWord(
        reason: String,
        wordId: String,
        wordLength: Int,
        word: String
    ) {

        viewModelScope.launch(Dispatchers.IO) {

            reportWordUseCase(
                wordLength = wordLength,
                wordId = wordId,
                reportedWord = ReportWordDto(
                    word = word,
                    reason = reason
                )
            ).collect {
                _state.value = it
            }

        }

    }

    fun resetState() { _state.value = ViewState.IDLE }

}