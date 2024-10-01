package com.carlostorres.wordsgame.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.home.presentation.GameSituations
import com.carlostorres.wordsgame.home.presentation.HomeEvents
import com.carlostorres.wordsgame.home.presentation.HomeViewModel
import com.carlostorres.wordsgame.home.ui.components.keyboard.GameKeyboard
import com.carlostorres.wordsgame.home.ui.components.keyboard.KeyboardButton
import com.carlostorres.wordsgame.home.ui.components.word_line.WordChar
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.home.ui.components.word_line.WordLine

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.setUpGame()
    }

    Scaffold(

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when (state.gameSituation) {
                GameSituations.GameLoading -> {
                    Dialog(onDismissRequest = { /*TODO*/ }) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
                GameSituations.GameInProgress -> {}
                GameSituations.GameLost -> {
                    Dialog(onDismissRequest = { viewModel.setUpGame() }) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(text = "Perdiste :c")
                                Text(text = "La palabra era: ${state.actualSecretWord}")

                                Button(
                                    onClick = {
                                        viewModel.setUpGame()
                                    }
                                ) {
                                    Text(text = "Jugar de nuevo")
                                }

                            }
                        }
                    }
                }
                GameSituations.GameWon -> {
                    Dialog(onDismissRequest = { viewModel.setUpGame() }) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(text = "GANASTE")

                                Button(onClick = { viewModel.setUpGame() }) {
                                    Text(text = "Jugar de nuevo")
                                }

                            }
                        }
                    }
                }

            }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "WORDS GAME",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                WordLine(
                    tryNumber = state.tryNumber,
                    inputText = state.inputText,
                    tryInfo = state.intento1,
                    numberRow = 0
                )

                WordLine(
                    tryNumber = state.tryNumber,
                    inputText = state.inputText,
                    tryInfo = state.intento2,
                    numberRow = 1
                )

                WordLine(
                    tryNumber = state.tryNumber,
                    inputText = state.inputText,
                    tryInfo = state.intento3,
                    numberRow = 2
                )

                WordLine(
                    tryNumber = state.tryNumber,
                    inputText = state.inputText,
                    tryInfo = state.intento4,
                    numberRow = 3
                )

                WordLine(
                    tryNumber = state.tryNumber,
                    inputText = state.inputText,
                    tryInfo = state.intento5,
                    numberRow = 4
                )

                Spacer(modifier = Modifier.weight(1f))

                GameKeyboard(
                    modifier = Modifier,
                    onButtonClick = { charClicked ->
                        if (state.inputText.length < 5) {
                            viewModel.onEvent(HomeEvents.OnInputTextChange(charClicked))
                        }
                    },
                    keyboard = state.keyboard
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                ) {

                    KeyboardButton(
                        modifier = Modifier.weight(1f),
                        char = "Aceptar",
                        onClick = { charClicked ->
                            if (state.inputText.length == 5 && state.tryNumber < 5) {
                                viewModel.onEvent(HomeEvents.OnAcceptClick)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    KeyboardButton(
                        modifier = Modifier.weight(1f),
                        char = "borrar",
                        onClick = { charClicked ->
                            viewModel.onEvent(HomeEvents.OnDeleteClick)
                        }
                    )

                }

            }

        }

    }

}