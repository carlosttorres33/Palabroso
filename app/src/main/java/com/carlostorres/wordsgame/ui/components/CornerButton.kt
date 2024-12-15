package com.carlostorres.wordsgame.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.ROUND_CORNER_SIZE
import com.carlostorres.wordsgame.utils.ButtonPlace

@Composable
fun CornerButton(
    modifier: Modifier = Modifier,
    buttonPlace: ButtonPlace,
    buttonText : String,
    textColor : Color,
    onClick: () -> Unit,
) {

    val rounded = when(buttonPlace){
        ButtonPlace.BottomEnd -> RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 0.dp,
            bottomEnd = ROUND_CORNER_SIZE.dp
        )
        ButtonPlace.BottomStart -> RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = ROUND_CORNER_SIZE.dp,
            bottomEnd = 0.dp
        )
        ButtonPlace.Center -> RoundedCornerShape(0.dp)
        ButtonPlace.Bottom -> RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = ROUND_CORNER_SIZE.dp,
            bottomEnd = ROUND_CORNER_SIZE.dp
        )
    }

    Button(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray
            else LightBackgroundGray
        ),
        shape = rounded,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp
        ),
        onClick = {
            onClick()
        }
    ) {

        Text(
            text = buttonText.uppercase(),
            fontWeight = FontWeight.Bold,
            color = textColor,
        )

    }

}