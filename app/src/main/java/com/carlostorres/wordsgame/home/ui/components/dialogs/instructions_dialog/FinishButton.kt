package com.carlostorres.wordsgame.home.ui.components.dialogs.instructions_dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FinishButton(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onClick: () -> Unit,
    ) {

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {

        AnimatedVisibility(
            enter = slideInHorizontally (
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 200)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 200)
            ),
            visible = pagerState.currentPage == 2
        ) {

            Button(
                modifier = Modifier
                    .fillMaxSize(),
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGreen,
                )
            ) {

                Text(
                    text = "OK!",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

            }

        }
    }
}