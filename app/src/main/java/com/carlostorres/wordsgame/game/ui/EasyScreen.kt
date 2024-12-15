package com.carlostorres.wordsgame.game.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.icu.util.Calendar
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.game.presentation.GameEvents
import com.carlostorres.wordsgame.game.presentation.easy.EasyViewModel
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.CoinsCounter
import com.carlostorres.wordsgame.ui.components.CountBox
import com.carlostorres.wordsgame.ui.components.HintBox
import com.carlostorres.wordsgame.ui.components.dialogs.BuyHintDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameErrorDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameLoseDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GameWinDialog
import com.carlostorres.wordsgame.ui.components.dialogs.GetCoinsDialog
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
import com.carlostorres.wordsgame.ui.theme.TOP_BAR_HEIGHT
import com.carlostorres.wordsgame.utils.Constants.KEYBOARD_HINT_PRICE
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import com.carlostorres.wordsgame.utils.Constants.ONE_LETTER_HINT_PRICE
import com.carlostorres.wordsgame.utils.GameSituations
import com.carlostorres.wordsgame.utils.HintType
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasyScreen(
    viewModel: EasyViewModel = hiltViewModel(),
    onHomeClick: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity

    val state = viewModel.state

    val userDailyStats = viewModel.dailyStats.collectAsState(
        initial = UserDailyStats(
            easyGamesPlayed = 0,
            normalGamesPlayed = 0,
            hardGamesPlayed = 0,
            lastPlayedDate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        )
    )

    val winsCont = viewModel.gameWinsCount.collectAsState(initial = 0)
    val losesCont = viewModel.gameLostCount.collectAsState(initial = 0)

    var showWordAlreadyTried by remember {
        mutableStateOf(false)
    }

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
                modifier = Modifier.height(TOP_BAR_HEIGHT.dp),
                title = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "PALABROSO",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.showInterstitial(
                                activity,
                                navHome = {
                                    onHomeClick()
                                },
                                ifBack = true
                            )
                        }
                    ) {
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
                ),
                actions = {
                    CoinsCounter(
                        icon = R.drawable.coins,
                        coinsRemaining = state.userCoins,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(100.dp)
                    ) {
                        viewModel.showCoinsDialog(true)
                    }
                }
            )
        }
    ) { paddingValues ->

        BackHandler {
            viewModel.showInterstitial(
                activity = activity,
                navHome = {
                    onHomeClick()
                },
                ifBack = true
            )
        }

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
                getOneLetterHint,
                disableLettersHint
            ) = createRefs()

            //region Game Situations Dialogs
            AnimatedContent(state.gameSituation, label = "") { situation ->
                when (situation) {
                    GameSituations.GameLoading -> {
                        LoadingDialog()
                    }

                    GameSituations.GameInProgress -> {}

                    GameSituations.GameLost -> {
                        GameLoseDialog(
                            secretWord = state.secretWord,
                            onRetryClick = {
                                viewModel.showInterstitial(
                                    activity,
                                    navHome = {
                                        onHomeClick()
                                    }
                                )
                            },
                            onHomeClick = {
                                viewModel.showInterstitial(
                                    activity,
                                    navHome = {
                                        onHomeClick()
                                    },
                                    ifBack = true
                                )
                            },
                            isGameLimitReached = userDailyStats.value.easyGamesPlayed >= NUMBER_OF_GAMES_ALLOWED
                        )
                    }

                    GameSituations.GameWon -> {
                        GameWinDialog(
                            onRematchClick = {
                                viewModel.setUpGame()
                            },
                            onHomeClick = {
                                viewModel.showInterstitial(
                                    activity,
                                    navHome = {
                                        onHomeClick()
                                    },
                                    ifBack = true
                                )
                            },
                            isGameLimitReached = userDailyStats.value.easyGamesPlayed >= NUMBER_OF_GAMES_ALLOWED
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

            if (showWordAlreadyTried) {
                WordAlreadyTriedDialog(onDismiss = { showWordAlreadyTried = false })
            }

            if (state.showCoinsDialog) {
                GetCoinsDialog(
                    onAcceptClick = {
                        viewModel.showRewardedAd(activity, actualUserCoins = state.userCoins)
                        viewModel.showCoinsDialog(false)
                    },
                    onCancelClick = {
                        viewModel.showCoinsDialog(false)
                    }
                )
            }

            if (state.showKeyboardHintDialog){
                BuyHintDialog(
                    hintType = HintType.KEYBOARD,
                    onDismiss = {
                        viewModel.hintDialogHandler(
                            hintType = HintType.KEYBOARD,
                            show = false
                        )
                    },
                    onAccept = {
                        viewModel.disable4KeyboardLettersHint(state.userCoins)
                        viewModel.hintDialogHandler(
                            hintType = HintType.KEYBOARD,
                            show = false
                        )
                    }
                )
            }

            if (state.showLetterHintDialog){
                BuyHintDialog(
                    hintType = HintType.ONE_LETTER,
                    onDismiss = {
                        viewModel.hintDialogHandler(
                            hintType = HintType.ONE_LETTER,
                            show = false
                        )
                    },
                    onAccept = {
                        viewModel.getOneLetterWord(state.userCoins)
                        viewModel.hintDialogHandler(
                            hintType = HintType.ONE_LETTER,
                            show = false
                        )
                    }
                )

            }

            //endregion

            CountBox(
                modifier = Modifier.constrainAs(winsCounter) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
                char = 'W',
                count = winsCont.value,
                color = if (isSystemInDarkTheme()) DarkGreen else LightGreen
            )

            HintBox(
                modifier = Modifier
                    .constrainAs(getOneLetterHint) {
                        top.linkTo(winsCounter.top)
                        bottom.linkTo(loseCounter.bottom)
                        start.linkTo(winsCounter.end)
                        end.linkTo(loseCounter.start)
                        height = Dimension.fillToConstraints
                    }
                    .aspectRatio(1f),
                icon = R.drawable.text_magnifying_glass,
                hintCoast = ONE_LETTER_HINT_PRICE,
                clickEnabled = state.userCoins >= ONE_LETTER_HINT_PRICE
            ) {
                viewModel.hintDialogHandler(HintType.ONE_LETTER, true)
            }

            HintBox(
                modifier = Modifier
                    .constrainAs(disableLettersHint) {
                        top.linkTo(winsCounter.top)
                        bottom.linkTo(loseCounter.bottom)
                        end.linkTo(loseCounter.start)
                        start.linkTo(getOneLetterHint.end)
                        height = Dimension.fillToConstraints
                    },
                icon = R.drawable.packages,
                hintCoast = KEYBOARD_HINT_PRICE,
                clickEnabled = state.userCoins >= KEYBOARD_HINT_PRICE
            ) {
                viewModel.hintDialogHandler(HintType.KEYBOARD, true)
            }

            CountBox(
                modifier = Modifier.constrainAs(loseCounter) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
                char = 'L',
                count = losesCont.value,
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

                val boxWidth = maxWidth / 4
                val boxHeight = maxHeight / 5

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Spacer(modifier = Modifier.weight(1f))

                    //region first try

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        repeat(4) { index ->

                            if (state.tryNumber == 0) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = WordCharState.Empty,
                                    char = state.inputList[index],
                                    isTurn = true,
                                    onFocusClick = {
                                        viewModel.onEvent(GameEvents.OnFocusChange(index))
                                    },
                                    isFocused = state.indexFocused == index
                                )
                            } else if (state.tryNumber > 0) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = state.intento1.resultado[index].second,
                                    char = state.intento1.resultado[index].first[0],
                                    isTurn = false
                                )
                            } else {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    char = null
                                )
                            }

                        }

                    }
                    //endregion

                    //region second try
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        repeat(4) { index ->

                            if (state.tryNumber == 1) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = WordCharState.Empty,
                                    char = state.inputList[index],
                                    isTurn = true,
                                    onFocusClick = {
                                        viewModel.onEvent(GameEvents.OnFocusChange(index))
                                    },
                                    isFocused = state.indexFocused == index
                                )
                            } else if (state.tryNumber > 1) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = state.intento2.resultado[index].second,
                                    char = state.intento2.resultado[index].first[0],
                                )
                            } else {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    char = null
                                )
                            }

                        }

                    }
                    //endregion

                    //region third try
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        repeat(4) { index ->

                            if (state.tryNumber == 2) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = WordCharState.Empty,
                                    char = state.inputList[index],
                                    isTurn = true,
                                    onFocusClick = {
                                        viewModel.onEvent(GameEvents.OnFocusChange(index))
                                    },
                                    isFocused = state.indexFocused == index
                                )
                            } else if (state.tryNumber > 2) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = state.intento3.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                    char = state.intento3.resultado[index].first[0], //state.intento1.word[index].toString()
                                )
                            } else {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    char = null
                                )
                            }

                        }

                    }
                    //endregion

                    //region furth try
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        repeat(4) { index ->

                            if (state.tryNumber == 3) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = WordCharState.Empty,
                                    char = state.inputList[index],
                                    isTurn = true,
                                    onFocusClick = {
                                        viewModel.onEvent(GameEvents.OnFocusChange(index))
                                    },
                                    isFocused = state.indexFocused == index
                                )
                            } else if (state.tryNumber > 3) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = state.intento4.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                                    char = state.intento4.resultado[index].first[0], //state.intento1.word[index].toString()
                                )
                            } else {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    char = null
                                )
                            }

                        }

                    }
                    //endregion

                    //region Fifth try
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        repeat(4) { index ->

                            if (state.tryNumber == 4) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = WordCharState.Empty,
                                    char = state.inputList[index],
                                    isTurn = true,
                                    onFocusClick = {
                                        viewModel.onEvent(GameEvents.OnFocusChange(index))
                                    },
                                    isFocused = state.indexFocused == index
                                )
                            } else if (state.tryNumber > 4) {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    charState = state.intento5.resultado[index].second,
                                    char = state.intento5.resultado[index].first[0],
                                )
                            } else {
                                WordChar(
                                    modifier = Modifier
                                        .height(
                                            if (boxHeight > boxWidth) boxWidth else boxHeight
                                        )
                                        .width(boxWidth),
                                    char = null
                                )
                            }

                        }

                    }
                    //endregion

                    Spacer(modifier = Modifier.weight(1f))

                }
            }

            BannerAd(
                modifier = Modifier
                    .constrainAs(bannerAd) {
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
                    viewModel.onEvent(
                        GameEvents.OnKeyboardClick(
                            charClicked[0],
                            state.indexFocused
                        )
                    )
                },
                keyboard = state.keyboard,
                onAcceptClick = {
                    if (state.wordsTried.contains(state.inputList.joinToString(""))) {
                        showWordAlreadyTried = true
                    } else {
                        viewModel.onEvent(GameEvents.OnAcceptClick(state.userCoins))
                    }
                },
                onAcceptState = if (state.inputList.none { it == null }) ButtonType.Unclicked else ButtonType.IsNotInWord,
                onBackspaceClick = {
                    viewModel.onEvent(GameEvents.OnKeyboardDeleteClick)
                }
            )

        }
    }
}