package com.carlostorres.wordsgame.home.ui.components.keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GameKeyboard(
    modifier: Modifier = Modifier,
    onButtonClick: (String) -> Unit
) {

    val abecedario = listOf(
        "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
        "A", "S", "D", "F", "G", "H", "J", "K", "L", "Ñ",
        "Z", "X", "C", "V", "B", "N", "M"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(10),
            verticalItemSpacing = 6.dp,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            items(abecedario){

                KeyboardButton(
                    char = it,
                    onClick = { charClicked ->
                        onButtonClick(charClicked)
                    },
                    type = ButtonType.Unclicked
                )

            }

        }

    }
    
}

@Preview (showSystemUi = true)
@Composable
private fun GameKeyboardPreview() {

    GameKeyboard(onButtonClick = {})
    
}