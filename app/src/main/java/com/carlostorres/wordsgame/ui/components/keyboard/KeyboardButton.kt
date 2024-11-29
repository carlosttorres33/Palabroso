package com.carlostorres.wordsgame.ui.components.keyboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkCustomGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightCustomGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightYellow

@Composable
fun KeyboardButton(
    modifier: Modifier = Modifier,
    char: String,
    onClick: (String) -> Unit,
    type: ButtonType = ButtonType.Unclicked
) {

    Card(
        modifier = modifier
            .bounceClick(type != ButtonType.IsNotInWord)
            .clickable {
                if (type != ButtonType.IsNotInWord) {
                    onClick(char)
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = when (type) {
                ButtonType.IsOnWord -> if (isSystemInDarkTheme()) DarkYellow else LightYellow
                ButtonType.IsNotInWord -> if (isSystemInDarkTheme()) DarkCustomGray else LightCustomGray
                ButtonType.IsOnPosition -> if (isSystemInDarkTheme()) DarkGreen else LightGreen
                ButtonType.Unclicked -> if (isSystemInDarkTheme()) DarkBackgroundGray  else Color.White
            }
        ),
        border = BorderStroke(1.dp, color = Color.Black),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = char.uppercase(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun KeyboardButtonPreview() {

    Row(modifier = Modifier.fillMaxSize()) {

        KeyboardButton(
            modifier = Modifier.weight(1f),
            char = "A",
            onClick = {},
            type = ButtonType.IsNotInWord
        )

        KeyboardButton(
            modifier = Modifier.weight(1f),
            char = "delete",
            onClick = { charClicked ->
            }
        )
    }


}

sealed class ButtonType {
    object IsOnWord : ButtonType()
    object IsOnPosition : ButtonType()
    object IsNotInWord : ButtonType()
    object Unclicked : ButtonType()
}