package com.carlostorres.wordsgame.home.ui.components.word_line

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlostorres.wordsgame.home.ui.components.keyboard.ButtonType

@Composable
fun WordChar(
    modifier: Modifier = Modifier,
    charState: WordCharState = WordCharState.Empty,
    char: String,
    isTurn: Boolean = false
) {

    Card(
        modifier = modifier
            .height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = when(charState){
                WordCharState.Empty -> Color.White
                WordCharState.IsNotInWord -> Color.Gray
                WordCharState.IsOnPosition -> Color.Green
                WordCharState.IsOnWord -> Color.Yellow
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, if (isTurn)Color.Green else Color.Black)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){

            Text(
                text = char,
                fontSize = 30.sp,
                color = Color.Black,
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
    object Empty: WordCharState()
    object IsOnWord: WordCharState()
    object IsOnPosition: WordCharState()
    object IsNotInWord: WordCharState()
}
