package com.carlostorres.wordsgame.home.ui.components.keyboard

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.bounceClick

@Composable
fun ActionKeyboardButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon : Int,
    onClick: () -> Unit,
    type: ButtonType = ButtonType.Unclicked
) {

    Card(
        modifier = modifier
            .bounceClick()
            .clickable {
                if (type != ButtonType.IsNotInWord) {
                    onClick()
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = when (type) {
                ButtonType.IsOnWord -> Color.Yellow
                ButtonType.IsNotInWord -> Color.Gray
                ButtonType.IsOnPosition -> Color.Green
                ButtonType.Unclicked -> Color.White
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
            Icon(painter = painterResource(icon), contentDescription = "", modifier = Modifier.fillMaxSize())
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