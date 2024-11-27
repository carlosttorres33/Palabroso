package com.carlostorres.wordsgame.game.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.game.presentation.hard.HardViewModel
import com.carlostorres.wordsgame.game.presentation.hard.HardEvents
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.CountBox
import com.carlostorres.wordsgame.ui.components.dialogs.GameErrorDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameLimitDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameLoseDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameWinDialog
import com.carlostorres.wordsgame.ui.components.dialogs.LoadingDialog
import com.carlostorres.wordsgame.ui.components.dialogs.WordAlreadyTriedDialog
import com.carlostorres.wordsgame.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.ui.components.keyboard.GameKeyboard
import com.carlostorres.wordsgame.ui.components.word_line.WordChar
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import com.carlostorres.wordsgame.utils.GameSituations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HardScreen(
    viewModel: HardViewModel = hiltViewModel(),
    onHomeClick: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity

    val state = viewModel.state

    var showWordAlreadyTried by remember {
        mutableStateOf(false)
    }

    val userDailyStats by viewModel.userDailyStats.collectAsState()

    val requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    LaunchedEffect(key1 = requestedOrientation) {
        activity?.requestedOrientation = requestedOrientation
    }

    LaunchedEffect(Unit) {
        if (state.secretWord.isEmpty()) {
            viewModel.setUpGame()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
        else LightBackgroundGray,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "PALABROSO",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onHomeClick()
                        }
                    ){
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "",
                            tint = if (isSystemInDarkTheme()) LightBackgroundGray else DarkBackgroundGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
                    else LightBackgroundGray
                )
            )
        }
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
                bannerAd,
            ) = createRefs()

            //region Game Situations Dialogs

            if (showWordAlreadyTried) {
                WordAlreadyTriedDialog(onDismiss = { showWordAlreadyTried = false })
            }

            AnimatedContent(state.gameSituation, label = "") { situation ->
                when (situation) {
                    GameSituations.GameLoading -> {
                        LoadingDialog()
                    }

                    GameSituations.GameInProgress -> {
                        if (userDailyStats.hardGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                            GameLimitDialog {
                                viewModel.showInterstitial(
                                    activity,
                                    navHome = { onHomeClick() }
                                )
                            }
                        }
                    }

                    GameSituations.GameLost -> {
                        GameLoseDialog(
                            secretWord = state.secretWord,
                            onRetryClick = {
                                viewModel.showInterstitial(activity, navHome = {onHomeClick()})
                            },
                            onHomeClick = {
                                viewModel.showInterstitial(activity, navHome = {onHomeClick()})
                            },
                            isGameLimitReached = userDailyStats.hardGamesPlayed >= NUMBER_OF_GAMES_ALLOWED
                        )
                    }

                    GameSituations.GameWon -> {
                        GameWinDialog(
                            onRematchClick = {
                                viewModel.showInterstitial(activity,navHome = {onHomeClick()})
                            },
                            onHomeClick = {
                                viewModel.showInterstitial(activity,navHome = {onHomeClick()})
                            },
                            isGameLimitReached = userDailyStats.hardGamesPlayed >= NUMBER_OF_GAMES_ALLOWED
                        )
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

            CountBox(
                modifier = Modifier.constrainAs(loseCounter) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                char = 'L',
                count = state.gameLostCount,
                color = if (isSystemInDarkTheme()) DarkRed else LightRed
            )

            BoxWithConstraints(
                modifier = Modifier
                    .constrainAs(boardContainer) {
                        top.linkTo(loseCounter.bottom, margin = 8.dp)
                        bottom.linkTo(bannerAd.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {

                val maxWidth = this.maxWidth
                val maxHeight = this.maxHeight

                val boxWidth = maxWidth / 6
                val boxHeight = maxHeight / 6

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

                            repeat(6) { index ->

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

                            repeat(6) { index ->

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

                            repeat(6) { index ->

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

                            repeat(6) { index ->

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

                            repeat(6) { index ->

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

                    //region six try
                    BasicTextField(
                        value = state.inputText,
                        onValueChange = {},
                        enabled = false,
                        singleLine = true
                    ){

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            repeat(6) { index ->

                                val char = when {
                                    index >= state.inputText.length -> ""
                                    else -> state.inputText[index].toString()
                                }

                                if (state.tryNumber == 5) {
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
                                } else if (state.tryNumber > 5) {
                                    WordChar(
                                        modifier = Modifier
                                            .height(
                                                if (boxHeight > boxWidth) boxWidth else boxHeight
                                            )
                                            .width(boxWidth),
                                        charState = state.intento6.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                        char = state.intento6.resultado[index].first, //state.intento1.word[index].toString()
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
                        height = Dimension.value(50.dp)
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
                    if (state.inputText.length < 6) {
                        viewModel.onEvent(HardEvents.OnInputTextChange(charClicked))
                    }
                },
                keyboard = state.keyboard,
                onAcceptClick = {
                    if (state.wordsTried.contains(state.inputText)) {
                        showWordAlreadyTried = true
                    } else {
                        viewModel.onEvent(HardEvents.OnAcceptClick)
                    }
                },
                onAcceptState = if (state.inputText.length == 6) ButtonType.Unclicked else ButtonType.IsNotInWord,
                onBackspaceClick = {
                    viewModel.onEvent(HardEvents.OnDeleteClick)
                }
            )

        }

    }

}