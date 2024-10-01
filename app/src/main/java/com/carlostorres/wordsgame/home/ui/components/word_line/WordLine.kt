package com.carlostorres.wordsgame.home.ui.components.word_line

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.home.presentation.TryInfo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WordLine(
    modifier: Modifier = Modifier,
    tryNumber: Int,
    inputText: String,
    tryInfo: TryInfo,
    numberRow: Int
) {

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 12.dp),
        columns = StaggeredGridCells.Fixed(5),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        items(5) { index ->

            if (tryNumber == 0) {

                if (tryNumber == 0) {
                    WordChar(
                        modifier = Modifier,
                        charState = WordCharState.Empty,
                        char = if (index < inputText.length) inputText[index].toString() else "",
                        isTurn = true
                    )
                } else {
                    WordChar(
                        modifier = Modifier,
                        charState = tryInfo.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                        char = tryInfo.resultado[index].first, //state.intento1.word[index].toString()
                        isTurn = false
                    )
                }

            }else{
                AnimatedContent(
                    targetState = tryInfo,
                    label = "",
                    transitionSpec = {
                        fadeIn(animationSpec = tween(durationMillis = 2000)) with
                                fadeOut(animationSpec = tween(durationMillis = 2000))
                    }
                ) { target ->

                    if (tryNumber == numberRow) {
                        WordChar(
                            modifier = Modifier,
                            charState = WordCharState.Empty,
                            char = if (inputText.isEmpty()) {
                                ""
                            } else {
                                if (index < inputText.length) {
                                    inputText[index].toString()
                                } else ""
                            },
                            isTurn = true
                        )
                    } else if (tryNumber < numberRow) {
                        WordChar(
                            modifier = Modifier,
                            charState = tryInfo.resultado[index].second,
                            char = tryInfo.resultado[index].first,
                        )
                    } else {
                        WordChar(char = "")
                    }
                }
            }



        }


    }

}