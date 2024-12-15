package com.carlostorres.wordsgame.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UnderlinedButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    color: Color,
    text: String,
    onClick: () -> Unit
    ) {

    Column(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .clickable(isSelected) {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(vertical = 6.dp),
            text = text.uppercase(),
            color = color,
            fontWeight = FontWeight.Bold
        )

        AnimatedContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            targetState = isSelected,
            label = ""
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color =
                        if (!it) {
                            color.copy(
                                alpha = 0.8f
                            )
                        } else {
                            Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .height(2.dp)
                    .fillMaxSize()
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun UnderlinedButtonPreview() {
    UnderlinedButton(
        modifier = Modifier
            .width(200.dp),
        isSelected = true,
        color = Color.Red,
        text = "5 letras",
        onClick = {}
    )
}