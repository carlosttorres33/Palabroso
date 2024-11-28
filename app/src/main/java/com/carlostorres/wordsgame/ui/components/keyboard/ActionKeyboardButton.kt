package com.carlostorres.wordsgame.ui.components.keyboard

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.pressClickEffect
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightCustomGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightYellow

@Composable
fun ActionKeyboardButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon : Int,
    onClick: () -> Unit,
    type: ButtonType = ButtonType.Unclicked
) {

    Card(
        modifier = modifier
            .bounceClick(type != ButtonType.IsNotInWord)
            .clickable {
                if (type != ButtonType.IsNotInWord) {
                    onClick()
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = when (type) {
                ButtonType.IsOnWord -> LightYellow
                ButtonType.IsNotInWord -> if (isSystemInDarkTheme()) Color.Black else LightCustomGray
                ButtonType.IsOnPosition -> LightGreen
                ButtonType.Unclicked -> if (isSystemInDarkTheme()) DarkBackgroundGray else Color.White
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
            Icon(
                painter = painterResource(icon),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun KeyboardButtonPreview() {

    Row(modifier = Modifier.fillMaxSize()) {

        ActionKeyboardButton(
            modifier = Modifier.weight(1f),
            icon = R.drawable.baseline_send_24,
            onClick = {},
            type = ButtonType.IsOnWord
        )

        ActionKeyboardButton(
            modifier = Modifier.weight(1f),
            icon = R.drawable.baseline_backspace_24,
            onClick = {},
            type = ButtonType.Unclicked
        )
    }


}