package com.carlostorres.wordsgame.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlostorres.wordsgame.game.domain.usecases.OnboardingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val useCases: OnboardingUseCases
) : ViewModel() {

    private val _hasSeenOnboarding = MutableStateFlow(false)
    val hasSeenOnboarding: StateFlow<Boolean> = _hasSeenOnboarding

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _hasSeenOnboarding.value =
                useCases.readInstructionsUseCase().stateIn(viewModelScope).value
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.saveInstructionsUseCase(true)
            _hasSeenOnboarding.value = true
        }
    }

}