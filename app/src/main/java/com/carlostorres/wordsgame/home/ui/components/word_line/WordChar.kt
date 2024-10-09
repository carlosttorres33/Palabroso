package com.carlostorres.wordsgame.home.ui.components.word_line

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlostorres.wordsgame.ui.theme.DarkCustomGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightCustomGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightYellow

@Composable
fun WordChar(
    modifier: Modifier = Modifier,
    charState: WordCharState = WordCharState.Empty,
    char: String,
    isTurn: Boolean = false
) {

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

    Card(
        modifier = modifier
            .height(72.dp)
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (charState) {
                WordCharState.Empty -> if (isSystemInDarkTheme()) Color.Black else LightBackgroundGray
                WordCharState.IsNotInWord -> if (isSystemInDarkTheme()) DarkCustomGray else LightCustomGray
                WordCharState.IsOnPosition -> if (isSystemInDarkTheme()) DarkGreen else LightGreen
                WordCharState.IsOnWord -> if (isSystemInDarkTheme()) DarkYellow else LightYellow
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isTurn) {
                if (isSystemInDarkTheme()) DarkGreen.copy(alpha = alphaAnim) else LightGreen.copy(alpha = alphaAnim)
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

@Preview(showSystemUi = true)
@Composable
private fun WordCharPreview() {
    WordChar(
        modifier = Modifier,
        charState = WordCharState.IsOnWord,
        char = "A"
    )
}

sealed class WordCharState {
    object Empty : WordCharState()
    object IsOnWord : WordCharState()
    object IsOnPosition : WordCharState()
    object IsNotInWord : WordCharState()
}
