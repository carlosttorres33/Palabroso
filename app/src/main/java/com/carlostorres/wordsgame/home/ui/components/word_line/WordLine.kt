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

            AnimatedContent(
                targetState = tryNumber,
                label = "",
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 2000)) with
                            fadeOut(animationSpec = tween(durationMillis = 2000))
                }
            ) { target ->

                when {
                    target > numberRow -> {
                        WordChar(
                            modifier = Modifier,
                            charState = tryInfo.resultado[index].second,// if (state.intento1.coincidences.contains(index)) WordCharState.IsOnPosition else WordCharState.Empty,
                            char = tryInfo.resultado[index].first, //state.intento1.word[index].toString()
                        )
                    }
                    else -> {
                        if (tryNumber == numberRow) {
                            WordChar(
                                modifier = Modifier,
                                charState = WordCharState.Empty,
                                char = if (index < inputText.length) inputText[index].toString() else "",
                                isTurn = true
                            )
                        }else{
                            WordChar(char = "")
                        }
                    }
                }
            }
            
        }

    }


}