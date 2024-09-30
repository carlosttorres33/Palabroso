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
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

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

            AnimatedContent(
                targetState = state.gameSituation,
                transitionSpec = {
                    // Entra deslizándose desde la izquierda
                    slideInHorizontally(
                        initialOffsetX = { -it }, // Comienza fuera de la pantalla a la izquierda
                        animationSpec = tween(durationMillis = 500) // Duración de 500ms
                    ) + fadeIn(animationSpec = tween(durationMillis = 500)) with
                            // Sale deslizándose hacia la derecha
                            slideOutHorizontally(
                                targetOffsetX = { it }, // Se mueve fuera de la pantalla a la derecha
                                animationSpec = tween(durationMillis = 500)
                            ) + fadeOut(animationSpec = tween(durationMillis = 500))
                },
                label = ""
            ) { situations ->

                when (situations) {
                    GameSituations.GameInProgress -> {

                    }
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
                    GameSituations.GameLoading -> {

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

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 12.dp),
                    columns = StaggeredGridCells.Fixed(5),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(5) { index ->

                        if (state.tryNumber == 0) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < state.inputText.length) state.inputText[index].toString() else ""
                            )
                        } else {
                            WordChar(
                                modifier = Modifier,
                                charState = state.intento1.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                char = state.intento1.resultado[index].first //state.intento1.word[index].toString()
                            )
                        }

                    }

                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 12.dp),
                    columns = StaggeredGridCells.Fixed(5),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(5) { index ->

                        if (state.tryNumber == 1) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < state.inputText.length) state.inputText[index].toString() else ""
                            )
                        } else if (state.tryNumber > 1) {
                            WordChar(
                                modifier = Modifier,
                                charState = state.intento2.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                char = state.intento2.resultado[index].first //state.intento1.word[index].toString()
                            )
                        } else {
                            WordChar(char = "")
                        }

                    }

                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 12.dp),
                    columns = StaggeredGridCells.Fixed(5),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(5) { index ->

                        if (state.tryNumber == 2) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < state.inputText.length) state.inputText[index].toString() else ""
                            )
                        } else if (state.tryNumber > 2) {
                            WordChar(
                                modifier = Modifier,
                                charState = state.intento3.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                char = state.intento3.resultado[index].first //state.intento1.word[index].toString()
                            )
                        } else {
                            WordChar(char = "")
                        }

                    }

                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 12.dp),
                    columns = StaggeredGridCells.Fixed(5),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(5) { index ->

                        if (state.tryNumber == 3) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < state.inputText.length) state.inputText[index].toString() else ""
                            )
                        } else if (state.tryNumber > 3) {
                            WordChar(
                                modifier = Modifier,
                                charState = state.intento4.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                char = state.intento4.resultado[index].first //state.intento1.word[index].toString()
                            )
                        } else {
                            WordChar(char = "")
                        }

                    }

                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 12.dp),
                    columns = StaggeredGridCells.Fixed(5),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(5) { index ->

                        if (state.tryNumber == 4) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < state.inputText.length) state.inputText[index].toString() else ""
                            )
                        } else if (state.tryNumber > 4) {
                            WordChar(
                                modifier = Modifier,
                                charState = state.intento5.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                char = state.intento5.resultado[index].first //state.intento1.word[index].toString()
                            )
                        } else {
                            WordChar(char = "")
                        }

                    }

                }

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