package com.carlostorres.wordsgame.onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.game.data.model.InstructionsPages
import com.carlostorres.wordsgame.onboarding.presentation.OnboardingViewModel
import com.carlostorres.wordsgame.onboarding.ui.components.OnboardingPager

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onFinish: () -> Unit,
    onFinishMenu: () -> Unit,
    isFromMenu: Boolean = false,
) {

    LaunchedEffect(viewModel.hasSeenOnboarding) {
        if (!isFromMenu) {
            if (viewModel.hasSeenOnboarding.value) {
                onFinish()
            }
        }
    }

    val pages = listOf(
        InstructionsPages.First,
        InstructionsPages.Second,
        InstructionsPages.Third,
        InstructionsPages.Fourth
    )

    OnboardingPager(
        pages = pages
    ) {
        if (isFromMenu) {
            onFinishMenu()
        } else {
            onFinish()
            viewModel.completeOnboarding()
        }
    }

}