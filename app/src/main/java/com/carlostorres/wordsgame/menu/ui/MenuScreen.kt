package com.carlostorres.wordsgame.menu.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import android.icu.util.Calendar
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.game.data.repository.UserDailyStats
import com.carlostorres.wordsgame.menu.presentation.MenuViewModel
import com.carlostorres.wordsgame.ui.components.HowToPlayButton
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.components.UpdateDialog
import com.carlostorres.wordsgame.ui.components.dialogs.instructions_dialog.InstructionsDialog
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.utils.Constants.NUMBER_OF_GAMES_ALLOWED
import java.text.SimpleDateFormat
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    onDifficultySelected: (GameDifficult) -> Unit,
    onHowToPlayClick: () -> Unit
) {

    val context = LocalContext.current
    val activity = context as Activity
    val requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    val state = viewModel.state

    val userDailyStats = viewModel.dailyStats.collectAsState(
        initial = UserDailyStats(
            easyGamesPlayed = 0,
            normalGamesPlayed = 0,
            hardGamesPlayed = 0,
            lastPlayedDate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
        )
    )

    val colorText = if (isSystemInDarkTheme()) DarkTextGray else Color.Black

    val currentDate = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)

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

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
        else LightBackgroundGray,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
                    else LightBackgroundGray
                ),
                title = {
                    Text(
                        modifier = Modifier,
                        text = "PALABROSO",
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
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
                howToPlayButton
            ) = createRefs()

            val chain = createVerticalChain(
                btnEasy, btnMedium, btnHard, btnSoon,
                chainStyle = ChainStyle.Packed
            )

            constrain(chain) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }

            Text(
                modifier = Modifier
                    .constrainAs(instructionsText) {
                        bottom.linkTo(btnEasy.top, margin = 18.dp)
                        start.linkTo(parent.start, margin = 18.dp)
                        end.linkTo(parent.end, margin = 18.dp)
                        width = Dimension.fillToConstraints
                    },
                text = "Selecciona el modo de juego",
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
                    Toast.makeText(context, "Ya jugaste todas las palabras de 4 letras de hoy", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    onDifficultySelected(GameDifficult.Easy)
                }
            }

            MyButton(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .constrainAs(btnMedium) {},
                difficult = GameDifficult.Medium,
                text = "5 Letras"
            ) {
                if (userDailyStats.value.normalGamesPlayed >= NUMBER_OF_GAMES_ALLOWED) {
                    Toast.makeText(context, "Ya jugaste todas las palabras de 5 letras de hoy", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    onDifficultySelected(GameDifficult.Medium)
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
                    Toast.makeText(context, "Ya jugaste todas las palabras de 6 letras de hoy", Toast.LENGTH_SHORT)
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

            if (state.blockVersion) {
                UpdateDialog()
            }

        }

    }

}