package com.carlostorres.wordsgame.stats.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.stats.presentation.OnStatsEvents
import com.carlostorres.wordsgame.stats.presentation.StatsViewModel
import com.carlostorres.wordsgame.stats.ui.components.CustomChart
import com.carlostorres.wordsgame.stats.ui.components.WordItem
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.components.UnderlinedButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.ui.theme.LightYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    onHomeClick: () -> Unit
) {

    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.getAllStats()
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
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {

                if (state.isError.isNullOrEmpty()) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(start = 15.dp, end = 15.dp)
                        ) {
                            item {
                                UnderlinedButton(
                                    modifier = Modifier.width(120.dp),
                                    color = if (isSystemInDarkTheme()) DarkGreen else LightGreen,
                                    isSelected = if (state.statsShowed == GameDifficult.Easy) false else true,
                                    text = "4 Letras",
                                    onClick = {
                                        viewModel.onEvent(OnStatsEvents.onStatClicked(GameDifficult.Easy))
                                    }
                                )
                            }
                            item {
                                UnderlinedButton(
                                    modifier = Modifier.width(120.dp),
                                    color = if (isSystemInDarkTheme()) DarkYellow else LightYellow,
                                    isSelected = if (state.statsShowed == GameDifficult.Normal) false else true,
                                    text = "5 Letras",
                                    onClick = {
                                        viewModel.onEvent(OnStatsEvents.onStatClicked(GameDifficult.Normal))
                                    }
                                )
                            }
                            item {
                                UnderlinedButton(
                                    modifier = Modifier.width(120.dp),
                                    color = if (isSystemInDarkTheme()) DarkRed else LightRed,
                                    isSelected = if (state.statsShowed == GameDifficult.Hard) false else true,
                                    text = "6 Letras",
                                    onClick = {
                                        viewModel.onEvent(OnStatsEvents.onStatClicked(GameDifficult.Hard))
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomChart(
                            modifier = Modifier.padding(16.dp),
                            barValue = when (state.statsShowed) {
                                GameDifficult.Easy -> {
                                    listOf(
                                        state.easyStats.filter { it.attempts == 0 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 1 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 2 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 3 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 4 && it.win }.size,
                                        state.easyStats.filter { !it.win }.size,
                                    )
                                }

                                GameDifficult.Hard -> {
                                    listOf(
                                        state.hardStats.filter { it.attempts == 0 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 1 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 2 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 3 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 4 && it.win }.size,
                                        state.hardStats.filter { !it.win }.size,
                                    )
                                }

                                GameDifficult.Normal -> {
                                    listOf(
                                        state.normalStats.filter { it.attempts == 0 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 1 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 2 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 3 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 4 && it.win }.size,
                                        state.normalStats.filter { !it.win }.size,
                                    )
                                }
                            },
                            xAxisScale = listOf("1", "2", "3", "4", "5", "L"),
                            total_amount = when (state.statsShowed) {
                                GameDifficult.Easy -> {
                                    listOf(
                                        state.easyStats.filter { it.attempts == 0 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 1 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 2 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 3 && it.win }.size,
                                        state.easyStats.filter { it.attempts == 4 && it.win }.size,
                                        state.easyStats.filter { !it.win }.size,
                                    ).maxOrNull() ?: 0
                                }

                                GameDifficult.Hard -> {
                                    listOf(
                                        state.hardStats.filter { it.attempts == 0 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 1 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 2 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 3 && it.win }.size,
                                        state.hardStats.filter { it.attempts == 4 && it.win }.size,
                                        state.hardStats.filter { !it.win }.size,
                                    ).maxOrNull() ?: 0
                                }

                                GameDifficult.Normal -> {
                                    listOf(
                                        state.normalStats.filter { it.attempts == 0 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 1 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 2 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 3 && it.win }.size,
                                        state.normalStats.filter { it.attempts == 4 && it.win }.size,
                                        state.normalStats.filter { !it.win }.size,
                                    ).maxOrNull() ?: 0
                                }
                            },
                        )

                        AnimatedContent(
                            modifier = Modifier
                                .weight(1f),
                            targetState = state.statsShowed,
                            label = ""
                        ) {

                            LazyColumn() {
                                when (it) {
                                    GameDifficult.Easy -> {
                                        items(state.easyStats) {
                                            WordItem(word = it.wordGuessed, isGuessed = it.win)
                                        }
                                    }

                                    GameDifficult.Hard -> {
                                        items(state.hardStats) {
                                            WordItem(word = it.wordGuessed, isGuessed = it.win)
                                        }
                                    }

                                    GameDifficult.Normal -> {
                                        items(state.normalStats) {
                                            WordItem(word = it.wordGuessed, isGuessed = it.win)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(
                        text = state.isError,
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)
                }

            }
        }

    }

}