package com.carlostorres.wordsgame.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.ui.theme.LightYellow

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    difficult: GameDifficult? = null,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {

    val buttonColors : Color = when(difficult){
        GameDifficult.Easy -> {
            if (isSystemInDarkTheme()) LightGreen
            else DarkGreen
        }
        GameDifficult.Medium -> {
            if (isSystemInDarkTheme()) LightYellow
            else DarkYellow
        }
        GameDifficult.Hard -> {
            if (isSystemInDarkTheme()) LightRed
            else DarkRed
        }
        else -> {
            Color.Black
        }
    }

    OutlinedButton(
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(),
        border = BorderStroke(2.dp, buttonColors),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
            else LightBackgroundGray,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        onClick = {
            onClick()
        }
    ) {

        Text(
            text = text.uppercase(),
            fontWeight = FontWeight.Bold,
            color = buttonColors,
        )

    }

}

sealed class GameDifficult(){
    object Easy : GameDifficult()
    object Medium : GameDifficult()
    object Hard : GameDifficult()
}

@Preview(showBackground = true)
@Composable
private fun PrevMyButton() {
    MyButton(
        difficult = GameDifficult.Easy,
        text = "Facil"
    ){}
}