package com.carlostorres.wordsgame.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray

@Composable
fun CountBox(
    modifier: Modifier = Modifier,
    char: Char,
    count: Int,
    color: Color
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Text(
            modifier = Modifier.padding(6.dp),
            text = "$char: $count",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }

}