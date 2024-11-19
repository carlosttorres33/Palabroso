package com.carlostorres.wordsgame.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray

@Composable
fun HowToPlayButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    IconButton(
        modifier = modifier
            .border(
                1.dp,
                if (isSystemInDarkTheme()) Color.White else Color.Black,
                RoundedCornerShape(12.dp)
            )
            .background(
                if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray,
                RoundedCornerShape(12.dp)
            ),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_question_mark_24),
            contentDescription = "",
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
        )
    }

}