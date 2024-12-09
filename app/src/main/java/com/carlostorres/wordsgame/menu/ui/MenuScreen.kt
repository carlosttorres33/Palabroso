package com.carlostorres.wordsgame.menu.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.menu.presentation.MenuViewModel
import com.carlostorres.wordsgame.ui.components.HowToPlayButton
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.CoinsCounter
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.components.UpdateDialog
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.TOP_BAR_HEIGHT
import com.carlostorres.wordsgame.utils.ConnectionStatus
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onDifficultySelected: (GameDifficult) -> Unit,
    onHowToPlayClick: () -> Unit,
    onStatsClick: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity
    val requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val state = viewModel.state

    val userDailyStats = viewModel.dailyStats.collectAsState()
    val canAccessToApp = viewModel.canAccessToApp.collectAsState()

    val colorText = if (isSystemInDarkTheme()) DarkTextGray else Color.Black

    val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)

    val isConnected by viewModel.isConnected.collectAsState()

    val showNoWords by remember {
        derivedStateOf {
            userDailyStats.value.easyGamesPlayed >= NUMBER_OF_GAMES_ALLOWED &&
                    userDailyStats.value.normalGamesPlayed >= NUMBER_OF_GAMES_ALLOWED &&
                    userDailyStats.value.hardGamesPlayed >= NUMBER_OF_GAMES_ALLOWED
        }
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.all_words_played_anim)
    )

    LaunchedEffect(key1 = requestedOrientation) {
        activity?.requestedOrientation = requestedOrientation
    }

    LaunchedEffect(Unit) {
        if (userDailyStats.value.lastPlayedDate != currentDate) {
            viewModel.updateDailyStats(
                UserDailyStats(
                    easyGamesPlayed = 0,
                    normalGamesPlayed = 0,
                    hardGamesPlayed = 0,
                    lastPlayedDate = currentDate
                )
            )
        }
    }

    LaunchedEffect(isConnected) {
        if (isConnected == ConnectionStatus.Lost) {
            snackBarHostState.showSnackbar(
                message = "No hay conexión a internet\nRecuerda que offline hay palabras limitadas",
                withDismissAction = true
            )
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
        else LightBackgroundGray,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TOP_BAR_HEIGHT.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
                    else LightBackgroundGray
                ),
                title = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier,
                            text = "PALABROSO",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    CoinsCounter(
                        icon = R.drawable.coins, coinsRemaining = 250, modifier = Modifier
                            .fillMaxHeight()
                            .width(100.dp)
                    ) {
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            val (
                bannerAd,
                btnEasy,
                btnMedium,
                btnHard,
                btnSoon,
                instructionsText,
                howToPlayButton,
                animationContainer,
                statsButton
            ) = createRefs()

            val chain = createVerticalChain(
                btnEasy, btnMedium, btnHard, btnSoon,
                chainStyle = ChainStyle.Packed
            )

            constrain(chain) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }

            Box(
                modifier = Modifier
                    .constrainAs(animationContainer) {
                        top.linkTo(parent.top, margin = 12.dp)
                        bottom.linkTo(instructionsText.top, margin = 12.dp)
                        start.linkTo(parent.start, margin = 12.dp)
                        end.linkTo(parent.end, margin = 12.dp)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    },
                contentAlignment = Alignment.BottomEnd
            ) {
                AnimatedVisibility(
                    showNoWords
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .fillMaxSize(),
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit,
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Animation by: naiandersonbruno",
                            color = colorText.copy(alpha = 0.7f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Text(
                modifier = Modifier
                    .constrainAs(instructionsText) {
                        bottom.linkTo(btnEasy.top, margin = 18.dp)
                        start.linkTo(parent.start, margin = 18.dp)
                        end.linkTo(parent.end, margin = 18.dp)
                        width = Dimension.fillToConstraints
                    },
                text = if (showNoWords) "Ya jugaste todas las palabras de hoy" else "Selecciona el modo de juego",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorText
            )

            MyButton(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .constrainAs(btnEasy) {},
                difficult = GameDifficult.Easy,
                text = "4 Letras"
            ) {
                if (userDailyStats.value.easyGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                    Toast.makeText(
                        context,
                        "Ya jugaste todas las palabras de 4 letras de hoy",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    onDifficultySelected(GameDifficult.Easy)
                }
            }

            MyButton(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .constrainAs(btnMedium) {},
                difficult = GameDifficult.Normal,
                text = "5 Letras"
            ) {
                if (userDailyStats.value.normalGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                    Toast.makeText(
                        context,
                        "Ya jugaste todas las palabras de 5 letras de hoy",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    onDifficultySelected(GameDifficult.Normal)
                }
            }

            MyButton(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .constrainAs(btnHard) {},
                difficult = GameDifficult.Hard,
                text = "6 Letras"
            ) {
                if (userDailyStats.value.hardGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                    Toast.makeText(
                        context,
                        "Ya jugaste todas las palabras de 6 letras de hoy",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    onDifficultySelected(GameDifficult.Hard)
                }
            }

            MyButton(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .constrainAs(btnSoon) {},
                enabled = false,
                text = "... proximamente ..."
            ) {}

            IconButton(
                modifier = Modifier
                    .constrainAs(statsButton) {
                        start.linkTo(parent.start, margin = 12.dp)
                        bottom.linkTo(howToPlayButton.bottom)
                        top.linkTo(howToPlayButton.top)
                        height = Dimension.fillToConstraints
                        width = Dimension.value(30.dp)
                    },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorText.copy(alpha = 0.7f)
                ),
                onClick = {
                    onStatsClick()
                }
            ) {
                Icon(imageVector = Icons.Default.BarChart, contentDescription = "")
            }

            HowToPlayButton(
                modifier = Modifier
                    .constrainAs(howToPlayButton) {
                        bottom.linkTo(bannerAd.top, margin = 12.dp)
                        end.linkTo(parent.end, margin = 18.dp)
                    }
            ) {
                onHowToPlayClick()
            }

            BannerAd(
                modifier = Modifier
                    .constrainAs(bannerAd) {
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                        height = Dimension.value(50.dp)
                    }
            )

            if (state.blockVersion || !canAccessToApp.value) {
                UpdateDialog()
            }

        }

    }

}