package com.carlostorres.wordsgame.home.ui.components.instructions_dialog

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.carlostorres.wordsgame.home.data.model.InstructionsPages
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun InstructionsDialog(
    onClick: () -> Unit
) {

    val pages = listOf(
        InstructionsPages.First,
        InstructionsPages.Second,
        InstructionsPages.Third
    )

    val pagerState = rememberPagerState()

    Dialog(
        onDismissRequest = { onClick() }
    ) {
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
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                HorizontalPager(
                    modifier = Modifier,
                    count = pages.size,
                    state = pagerState
                ) { actualPage ->

                    PagerContent(
                        page = pages[actualPage],
                        index = actualPage
                    )
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState
                )

                FinishButton(
                    pagerState = pagerState
                ) {
                    onClick()
                }

            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PID() {
    InstructionsDialog(onClick = {})
}