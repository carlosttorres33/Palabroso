package com.carlostorres.wordsgame.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CountBox(
    modifier: Modifier = Modifier,
    char: Char,
    count: Int,
    color: Color
) {

    Box(
        modifier = modifier
            .border(1.dp, color, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
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