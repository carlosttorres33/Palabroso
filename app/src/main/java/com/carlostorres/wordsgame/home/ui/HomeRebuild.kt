package com.carlostorres.wordsgame.home.ui

import android.widget.Space
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.carlostorres.wordsgame.home.presentation.HomeEvents
import com.carlostorres.wordsgame.home.presentation.HomeViewModel
import com.carlostorres.wordsgame.home.ui.components.keyboard.ButtonType
import com.carlostorres.wordsgame.home.ui.components.keyboard.GameKeyboard
import com.carlostorres.wordsgame.home.ui.components.word_line.WordChar
import com.carlostorres.wordsgame.home.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.ui.theme.DarkCustomGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightCustomGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightYellow

/**
 * @function HomeRebuild
 * Eliminar despues de las pruebas y antes de hacer un merge
 */
@Composable
fun HomeRebuild(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state = viewModel.state

    //region animations
    var isJumping by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isJumping) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isJumping = false } // Restablece isJumping cuando la animación termina
    )
    val transition = rememberInfiniteTransition(label = "")
    val alphaAnim by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutLinearInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )
    //endregion

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            val maxWidth = this.maxWidth
            val maxHeight = this.maxHeight

            val boxWidth = maxWidth / 5
            val boxHeight = maxHeight / 5

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            ) {

                BasicTextField(
                    modifier = Modifier
                        .height(boxHeight)
                        .background(Color.Cyan),
                    value = state.inputText,
                    onValueChange = {},
                    enabled = false,
                    singleLine = true
                ) {

                    Row() {

                        repeat(5) { index ->

                            val char = when {
                                index >= state.inputText.length -> ""
                                else -> state.inputText[index].toString()
                            }

                            WordChar(
                                modifier = Modifier
                                    .height(boxHeight)
                                    .width(boxWidth),
                                char = char
                            )

                        }
                    }
                }

                BasicTextField(
                    modifier = Modifier
                        .height(boxHeight)
                        .background(Color.Cyan),
                    value = state.inputText,
                    onValueChange = {},
                    enabled = false,
                    singleLine = true
                ) {

                    Row() {

                        repeat(5) { index ->

                            val char = when {
                                index >= state.inputText.length -> ""
                                else -> state.inputText[index].toString()
                            }

                            Card(
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .fillMaxHeight()
                                    .width(boxWidth),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSystemInDarkTheme()) DarkYellow else LightYellow
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 10.dp
                                ),
                                shape = RoundedCornerShape(28.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (true) {
                                        if (isSystemInDarkTheme()) DarkGreen.copy(alpha = alphaAnim) else LightGreen.copy(
                                            alpha = alphaAnim
                                        )
                                    } else Color.Black
                                )
                            ) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = char,
                                        fontSize = 30.sp,
                                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                                        fontWeight = FontWeight.Black
                                    )

                                }

                            }
                        }
                    }


                }

            }
        }

        Spacer(modifier = Modifier.weight(1f))

        GameKeyboard(
            modifier = Modifier,
            onButtonClick = { charClicked ->
                if (state.inputText.length < 5) {
                    viewModel.onEvent(HomeEvents.OnInputTextChange(charClicked))
                }
            },
            keyboard = state.keyboard,
            onAcceptClick = {
                if (state.wordsTried.contains(state.inputText)) {

                } else {
                    viewModel.onEvent(HomeEvents.OnAcceptClick)
                }
            },
            onAcceptState = if (state.inputText.length == 5) ButtonType.Unclicked else ButtonType.IsNotInWord,
            onBackspaceClick = {
                viewModel.onEvent(HomeEvents.OnDeleteClick)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

    }

}

