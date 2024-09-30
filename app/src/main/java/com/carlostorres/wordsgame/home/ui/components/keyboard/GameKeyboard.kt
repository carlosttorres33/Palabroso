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
    onButtonClick: (String) -> Unit,
    keyboard: List<KeyboardChar> = emptyList()
) {

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

            items(keyboard){

                KeyboardButton(
                    char = it.char,
                    onClick = { charClicked ->
                        onButtonClick(charClicked)
                    },
                    type = it.type
                )

            }

        }

    }
    
}

data class KeyboardChar(
    val char: String,
    val type: ButtonType = ButtonType.Unclicked
)

@Preview (showSystemUi = true)
@Composable
private fun GameKeyboardPreview() {

    GameKeyboard(onButtonClick = {})
    
}