package com.carlostorres.wordsgame.home.ui

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.home.presentation.GameSituations
import com.carlostorres.wordsgame.home.presentation.HomeEvents
import com.carlostorres.wordsgame.home.presentation.HomeViewModel
import com.carlostorres.wordsgame.home.ui.components.CountBox
import com.carlostorres.wordsgame.home.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.home.ui.components.keyboard.GameKeyboard
import com.carlostorres.wordsgame.home.ui.components.keyboard.KeyboardButton
import com.carlostorres.wordsgame.home.ui.components.word_line.WordChar
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val activity = LocalContext.current as Activity

    val state = viewModel.state

    var showWordAlreadyTried by remember {
        mutableStateOf(false)
    }

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
                    Dialog(onDismissRequest = {}) {
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
                                        viewModel.showInterstitial(activity)
                                    }
                                ) {
                                    Text(text = "Jugar de nuevo")
                                }

                            }
                        }
                    }
                }

                GameSituations.GameWon -> {

                    Dialog(onDismissRequest = {}) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(text = "GANASTE")

                                    Button(
                                        onClick = {
                                            viewModel.showInterstitial(activity)
                                        }
                                    ) {
                                        Text(text = "Jugar de nuevo")
                                    }

                                }
                            }
                        }
                        KonfettiView(
                            modifier = Modifier.fillMaxSize(),
                            parties = listOf(
                                Party(
                                    emitter = Emitter(duration = 4, TimeUnit.SECONDS).perSecond(30)
                                ),
                                Party(
                                    emitter = Emitter(duration = 4, TimeUnit.SECONDS).perSecond(30)
                                ),
                                Party(
                                    emitter = Emitter(duration = 4, TimeUnit.SECONDS).perSecond(30)
                                )
                            )
                        )
                    }
                }

            }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    CountBox(char = 'W', count = state.gameWinsCount, color = Color.Green)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier,
                        text = "WORDS GAME",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CountBox(char = 'L', count = state.gameLostCount, color = Color.Red)

                }

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

                        AnimatedContent(
                            targetState = state.tryNumber,
                            transitionSpec = {
                                // Define la animación aquí (fadeIn/fadeOut, slideIn/slideOut, etc.)
                                if (targetState > 0 && initialState <= 0) {
                                    slideInVertically() + fadeIn() with slideOutVertically() + fadeOut()
                                } else if (targetState <= 0 && initialState > 0) {
                                    slideInVertically(initialOffsetY = { it }) + fadeIn() with slideOutVertically(
                                        targetOffsetY = { -it }) + fadeOut()
                                } else {
                                    fadeIn() with fadeOut() // Sin animación si tryNumber no cambia de mayor a 1 o viceversa
                                }
                            }
                        ) { target ->

                            if (target <= 0) {
                                WordChar(
                                    modifier = Modifier,
                                    charState = WordCharState.Empty,
                                    char = if (index < state.inputText.length) state.inputText[index].toString() else "",
                                    isTurn = true
                                )
                            } else {
                                WordChar(
                                    modifier = Modifier,
//                                    charState = if (state.intento1.resultado.isNotEmpty()) {
//                                        state.intento1.resultado[index].second
//                                    } else {
//                                        WordCharState.Empty
//                                    },
                                    charState = state.intento1.resultado[index].second,
                                    char = state.intento1.resultado[index].first, //state.intento1.word[index].toString()
                                    isTurn = false
                                )
                            }

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

                        AnimatedContent(
                            targetState = state.tryNumber,
                            transitionSpec = {
                                // Define la animación aquí (fadeIn/fadeOut, slideIn/slideOut, etc.)
                                if (targetState > 1 && initialState <= 1) {
                                    slideInVertically() + fadeIn() with slideOutVertically() + fadeOut()
                                } else if (targetState <= 1 && initialState > 1) {
                                    slideInVertically(initialOffsetY = { it }) + fadeIn() with slideOutVertically(
                                        targetOffsetY = { -it }) + fadeOut()
                                } else {
                                    fadeIn() with fadeOut() // Sin animación si tryNumber no cambia de mayor a 1 o viceversa
                                }
                            }
                        ) { tryNumber ->
                            if (tryNumber == 1) {
                                WordChar(
                                    modifier = Modifier,
                                    charState = WordCharState.Empty,
                                    char = if (index < state.inputText.length) state.inputText[index].toString() else "",
                                    isTurn = true
                                )
                            } else if (tryNumber > 1) {
                                WordChar(
                                    modifier = Modifier,
                                    charState = state.intento2.resultado[index].second,
                                    char = state.intento2.resultado[index].first
                                )
                            } else {
                                WordChar(char = "")
                            }
                        }

//                        if (state.tryNumber == 1) {
//                            WordChar(
//                                modifier = Modifier,
//                                charState = WordCharState.Empty,
//                                char = if (index < state.inputText.length) state.inputText[index].toString() else "",
//                                isTurn = true
//                            )
//                        } else if (state.tryNumber > 1) {
//                            WordChar(
//                                modifier = Modifier,
//                                charState = state.intento2.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
//                                char = state.intento2.resultado[index].first, //state.intento1.word[index].toString()
//                            )
//                        } else {
//                            WordChar(char = "")
//                        }

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
                                char = if (index < state.inputText.length) state.inputText[index].toString() else "",
                                isTurn = true
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
                                char = if (index < state.inputText.length) state.inputText[index].toString() else "",
                                isTurn = true
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
                                char = if (index < state.inputText.length) state.inputText[index].toString() else "",
                                isTurn = true
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
                    keyboard = state.keyboard,
                    onAcceptClick = {
                        viewModel.onEvent(HomeEvents.OnAcceptClick)
                    },
                    onAcceptState = if (state.inputText.length == 5) ButtonType.Unclicked else ButtonType.IsNotInWord,
                    onBackspaceClick = {
                        viewModel.onEvent(HomeEvents.OnDeleteClick)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

            }

            if (showWordAlreadyTried) {
                Dialog(onDismissRequest = { showWordAlreadyTried = false }) {
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

                            Text(text = "Esa palabra ya la has intentado, intenta con otra")

                            Button(
                                onClick = {
                                    showWordAlreadyTried = false
                                }
                            ) {
                                Text(text = "OK")
                            }

                        }
                    }
                }
            }

        }

    }

}