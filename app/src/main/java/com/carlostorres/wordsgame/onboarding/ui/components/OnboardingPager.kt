package com.carlostorres.wordsgame.onboarding.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.game.data.model.InstructionsPages
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.components.dialogs.instructions_dialog.PagerContent
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.ui.theme.LightYellow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPager(
    modifier: Modifier = Modifier,
    pages: List<InstructionsPages>,
    onFinish: () -> Unit
) {

    val pagerState = rememberPagerState()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ){

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(8f),
                    count = pages.size,
                    state = pagerState
                ) { index ->

                    val info = pages[index]

                    PagerContent(
                        modifier = Modifier.fillMaxSize(),
                        page = info,
                        index = index
                    )

                }

                AnimatedContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    targetState = pagerState.currentPage == pages.lastIndex,
                    label = "Bottom Buttons"
                ) { isLastPage ->

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {

                        if (isLastPage){
                            MyButton(text = "Entendido") {
                                onFinish()
                            }
                        } else {
                            TextButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .bounceClick(),
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pages.lastIndex)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSystemInDarkTheme()) DarkRed else LightRed
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    10.dp
                                )
                            ) {

                                Text(
                                    text = "Saltar",
                                    color = Color.White
                                )

                            }

                            HorizontalPagerIndicator(
                                pagerState = pagerState,
                                activeColor = DarkYellow,
                                inactiveColor = LightYellow
                            )

                            TextButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .bounceClick(),
                                onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSystemInDarkTheme()) DarkGreen else LightGreen
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    10.dp
                                )
                            ) {

                                Text(
                                    text = "Siguiente",
                                    color = Color.White
                                )

                            }
                        }

                    }

                }

            }

        }

    }

}