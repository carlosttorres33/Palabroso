package com.carlostorres.wordsgame.home.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.home.presentation.GameSituations
import com.carlostorres.wordsgame.home.presentation.HomeEvents
import com.carlostorres.wordsgame.home.presentation.HomeViewModel
import com.carlostorres.wordsgame.home.ui.components.CountBox
import com.carlostorres.wordsgame.home.ui.components.instructions_dialog.InstructionsDialog
import com.carlostorres.wordsgame.home.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.home.ui.components.keyboard.GameKeyboard
import com.carlostorres.wordsgame.home.ui.components.word_line.WordChar
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkCustomGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val activity = LocalContext.current as Activity

    val state = viewModel.state

    val seenInstructions by viewModel.seenInstructions.collectAsState()

    var showWordAlreadyTried by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (state.actualSecretWord.isEmpty()) {
            viewModel.setUpGame()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackgroundGray)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray)
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
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
                            )
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "Perdiste :c",
                                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                                )
                                Text(
                                    text = "La palabra era: ${state.actualSecretWord}",
                                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                                )

                                Button(
                                    modifier = Modifier
                                        .bounceClick(),
                                    onClick = {
                                        viewModel.showInterstitial(activity)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSystemInDarkTheme()) Color.Black else DarkCustomGray
                                    )
                                ) {
                                    Text(
                                        text = "Jugar de nuevo",
                                        color = if (isSystemInDarkTheme()) Color.White else Color.White
                                    )
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
                                    .align(Alignment.Center),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
                                )
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    Text(
                                        text = "GANASTE",
                                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                                    )

                                    Button(
                                        modifier = Modifier
                                            .bounceClick(),
                                        onClick = {
                                            viewModel.showInterstitial(activity)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSystemInDarkTheme()) Color.Black else DarkCustomGray
                                        )
                                    ) {
                                        Text(
                                            text = "Jugar de nuevo",
                                            color = if (isSystemInDarkTheme()) Color.White else Color.White
                                        )
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

                    CountBox(
                        char = 'W',
                        count = state.gameWinsCount,
                        color = if (isSystemInDarkTheme()) DarkGreen else LightGreen
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier,
                        text = "WORDS GAME",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CountBox(
                        char = 'L',
                        count = state.gameLostCount,
                        color = if (isSystemInDarkTheme()) DarkRed else LightRed
                    )

                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        modifier = Modifier
                            .border(
                                1.dp,
                                if (isSystemInDarkTheme()) Color.White else Color.Black,
                                RoundedCornerShape(12.dp)
                            )
                            .background(
                                if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray,
                                RoundedCornerShape(12.dp)
                            ),
                        onClick = {
                            viewModel.seeInstructions(false)
                            //viewModel.saveSeenInstructionsState(false)
                            //viewModel.readInstructions()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_question_mark_24),
                            contentDescription = "",
                            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }
                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    columns = StaggeredGridCells.Fixed(5),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(5) { index ->

                        if (state.tryNumber == 0) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < state.inputText.length) state.inputText[index].toString() else "",
                                isTurn = true
                            )
                        } else {
                            WordChar(
                                modifier = Modifier,
                                charState = state.intento1.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                char = state.intento1.resultado[index].first, //state.intento1.word[index].toString()
                                isTurn = false
                            )
                        }

                    }

                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    columns = StaggeredGridCells.Fixed(5),
                    verticalItemSpacing = 10.dp,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(5) { index ->

                        if (state.tryNumber == 1) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < state.inputText.length) state.inputText[index].toString() else "",
                                isTurn = true
                            )
                        } else if (state.tryNumber > 1) {
                            WordChar(
                                modifier = Modifier,
                                charState = state.intento2.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                char = state.intento2.resultado[index].first, //state.intento1.word[index].toString()
                            )
                        } else {
                            WordChar(char = "")
                        }

                    }

                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
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
                        .padding(horizontal = 32.dp),
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
                        .padding(horizontal = 32.dp),
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
                        if (state.wordsTried.contains(state.inputText)) {
                            showWordAlreadyTried = true
                        } else {
                            viewModel.onEvent(HomeEvents.OnAcceptClick)
                        }
                    },
                    onAcceptState = if (state.inputText.length == 5) ButtonType.Unclicked else ButtonType.IsNotInWord,
                    onBackspaceClick = {
                        viewModel.onEvent(HomeEvents.OnDeleteClick)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

            }

            if (!state.seenInstructions){
                InstructionsDialog(
                    onClick = {
                        viewModel.saveSeenInstructionsState(seen = true)
                        viewModel.seeInstructions(true)
                        //viewModel.readInstructions()
                    }
                )
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
                                modifier = Modifier
                                    .bounceClick(),
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