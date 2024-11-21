package com.carlostorres.wordsgame.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray

@Composable
fun HowToPlayButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray,
        ),
        border = BorderStroke(2.dp, if (isSystemInDarkTheme()) Color.White else Color.Black),
        onClick = {
            onClick()
        }
    ) {
        Row {
            Text(
                text = "¿Cómo jugar?",
                fontSize = 15.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun HTPBP() {
    HowToPlayButton {

    }
}