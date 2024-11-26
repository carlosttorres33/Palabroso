package com.carlostorres.wordsgame.game.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.game.presentation.normal.NormalEvents
import com.carlostorres.wordsgame.game.presentation.normal.NormalViewModel
import com.carlostorres.wordsgame.ui.components.CountBox
import com.carlostorres.wordsgame.ui.components.HowToPlayButton
import com.carlostorres.wordsgame.ui.components.UpdateDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameLoseDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameWinDialog
import com.carlostorres.wordsgame.ui.components.dialogs.LoadingDialog
import com.carlostorres.wordsgame.ui.components.dialogs.instructions_dialog.InstructionsDialog
import com.carlostorres.wordsgame.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.ui.components.keyboard.GameKeyboard
import com.carlostorres.wordsgame.ui.components.word_line.WordChar
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.dialogs.GameErrorDialog
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.utils.GameSituations

@Composable
fun NormalScreen(
    viewModel: NormalViewModel = hiltViewModel(),
    onHomeClick: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity

    val state = viewModel.state

    val seenInstructions by viewModel.seenInstructions.collectAsState()

    var showWordAlreadyTried by remember {
        mutableStateOf(false)
    }

    val requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    LaunchedEffect(key1 = requestedOrientation) {
        activity?.requestedOrientation = requestedOrientation
    }

    LaunchedEffect(Unit) {
        if (state.actualSecretWord.isEmpty()) {
            viewModel.setUpGame()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
        else LightBackgroundGray
    ) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {

            val (
                boardContainer,
                gameKeyboard,
                winsCounter,
                loseCounter,
                gameTitle,
                howToButton,
                bannerAd
            ) = createRefs()

            //region Game Situations Dialogs
            AnimatedContent(state.gameSituation, label = "") { situation ->
                when (situation) {
                    GameSituations.GameLoading -> {
                        LoadingDialog()
                    }

                    GameSituations.GameInProgress -> {}
                    GameSituations.GameLost -> {
                        GameLoseDialog(secretWord = state.actualSecretWord) {
                            viewModel.showInterstitial(activity)
                        }
                    }

                    GameSituations.GameWon -> {
                        GameWinDialog {
                            viewModel.showInterstitial(activity)
                        }
                    }

                    is GameSituations.GameError -> {
                        GameErrorDialog(
                            textError = situation.errorMessage,
                            onRetryClick = {
                                viewModel.setUpGame()
                            },
                            onHomeClick = {
                                onHomeClick()
                            }
                        )
                    }
                }
            }

            if (!state.seenInstructions) {
                InstructionsDialog(
                    onClick = {
                        viewModel.saveSeenInstructionsState(seen = true)
                        viewModel.seeInstructions(true)
                        //viewModel.readInstructions()
                    }
                )
            }

            if (state.blockVersion) {
                UpdateDialog()
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
            //endregion

            CountBox(
                modifier = Modifier.constrainAs(winsCounter) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                char = 'W',
                count = state.gameWinsCount,
                color = if (isSystemInDarkTheme()) DarkGreen else LightGreen
            )

            Text(
                modifier = Modifier
                    .constrainAs(gameTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(winsCounter.start)
                        end.linkTo(loseCounter.end)
                        bottom.linkTo(loseCounter.bottom)
                        width = Dimension.fillToConstraints
                    },
                text = "PALABROSO",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            CountBox(
                modifier = Modifier.constrainAs(loseCounter) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                char = 'L',
                count = state.gameLostCount,
                color = if (isSystemInDarkTheme()) DarkRed else LightRed
            )

            HowToPlayButton(
                modifier = Modifier
                    .constrainAs(howToButton) {
                        top.linkTo(loseCounter.bottom, margin = 8.dp)
                        end.linkTo(parent.end)
                    }
            ) {
                viewModel.seeInstructions(false)
            }

            BoxWithConstraints(
                modifier = Modifier
                    .constrainAs(boardContainer) {
                        top.linkTo(howToButton.bottom, margin = 8.dp)
                        bottom.linkTo(bannerAd.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {

                val maxWidth = this.maxWidth
                val maxHeight = this.maxHeight

                val rangeWidth = max(0.dp, maxWidth)

                val boxWidth = maxWidth / 5
                val boxHeight = maxHeight / 5

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    //region first try
                    BasicTextField(
                        value = state.inputText,
                        onValueChange = {},
                        enabled = false,
                        singleLine = true
                    ){

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            repeat(5) { index ->

                                val char = when {
                                    index >= state.inputText.length -> ""
                                    else -> state.inputText[index].toString()
                                }

                                if (state.tryNumber == 0) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = WordCharState.Empty,
                                        char = char,
                                        isTurn = true
                                    )
                                } else {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = state.intento1.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                        char = state.intento1.resultado[index].first, //state.intento1.word[index].toString()
                                        isTurn = false
                                    )
                                }

                            }

                        }

                    }
                    //endregion

                    //region second try
                    BasicTextField(
                        value = state.inputText,
                        onValueChange = {},
                        enabled = false,
                        singleLine = true
                    ){

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            repeat(5) { index ->

                                val char = when {
                                    index >= state.inputText.length -> ""
                                    else -> state.inputText[index].toString()
                                }

                                if (state.tryNumber == 1) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = WordCharState.Empty,
                                        char = char,
                                        isTurn = true
                                    )
                                } else if (state.tryNumber > 1) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = state.intento2.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                        char = state.intento2.resultado[index].first, //state.intento1.word[index].toString()
                                    )
                                } else {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        char = ""
                                    )
                                }

                            }

                        }

                    }
                    //endregion

                    //region third try
                    BasicTextField(
                        value = state.inputText,
                        onValueChange = {},
                        enabled = false,
                        singleLine = true
                    ){

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            repeat(5) { index ->

                                val char = when {
                                    index >= state.inputText.length -> ""
                                    else -> state.inputText[index].toString()
                                }

                                if (state.tryNumber == 2) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = WordCharState.Empty,
                                        char = char,
                                        isTurn = true
                                    )
                                } else if (state.tryNumber > 2) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = state.intento3.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                        char = state.intento3.resultado[index].first, //state.intento1.word[index].toString()
                                    )
                                } else {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        char = ""
                                    )
                                }

                            }

                        }

                    }
                    //endregion

                    //region furth try
                    BasicTextField(
                        value = state.inputText,
                        onValueChange = {},
                        enabled = false,
                        singleLine = true
                    ){

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            repeat(5) { index ->

                                val char = when {
                                    index >= state.inputText.length -> ""
                                    else -> state.inputText[index].toString()
                                }

                                if (state.tryNumber == 3) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = WordCharState.Empty,
                                        char = char,
                                        isTurn = true
                                    )
                                } else if (state.tryNumber > 3) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = state.intento4.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                        char = state.intento4.resultado[index].first, //state.intento1.word[index].toString()
                                    )
                                } else {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        char = ""
                                    )
                                }

                            }

                        }

                    }
                    //endregion

                    //region fifth try
                    BasicTextField(
                        value = state.inputText,
                        onValueChange = {},
                        enabled = false,
                        singleLine = true
                    ){

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            repeat(5) { index ->

                                val char = when {
                                    index >= state.inputText.length -> ""
                                    else -> state.inputText[index].toString()
                                }

                                if (state.tryNumber == 4) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = WordCharState.Empty,
                                        char = char,
                                        isTurn = true
                                    )
                                } else if (state.tryNumber > 4) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = state.intento5.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                        char = state.intento5.resultado[index].first, //state.intento1.word[index].toString()
                                    )
                                } else {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        char = ""
                                    )
                                }

                            }

                        }

                    }
                    //endregion

                    Spacer(modifier = Modifier.weight(0.4f))

                }
            }

            BannerAd(
                modifier = Modifier
                    .constrainAs(bannerAd){
                        bottom.linkTo(gameKeyboard.top, margin = 18.dp)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    }
            )

            GameKeyboard(
                modifier = Modifier
                    .constrainAs(gameKeyboard) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                    },
                onButtonClick = { charClicked ->
                    if (state.inputText.length < 5) {
                        viewModel.onEvent(NormalEvents.OnInputTextChange(charClicked))
                    }
                },
                keyboard = state.keyboard,
                onAcceptClick = {
                    if (state.wordsTried.contains(state.inputText)) {
                        showWordAlreadyTried = true
                    } else {
                        viewModel.onEvent(NormalEvents.OnAcceptClick)
                    }
                },
                onAcceptState = if (state.inputText.length == 5) ButtonType.Unclicked else ButtonType.IsNotInWord,
                onBackspaceClick = {
                    viewModel.onEvent(NormalEvents.OnDeleteClick)
                }
            )

        }

    }

}