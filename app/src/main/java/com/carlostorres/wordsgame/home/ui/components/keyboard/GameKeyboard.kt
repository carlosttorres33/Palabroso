package com.carlostorres.wordsgame.home.ui.components.keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.R

@Composable
fun GameKeyboard(
    modifier: Modifier = Modifier,
    onButtonClick: (String) -> Unit,
    keyboard: List<KeyboardChar> = emptyList(),
    onAcceptClick: () -> Unit,
    onAcceptState: ButtonType,
    onBackspaceClick: () -> Unit,
) {

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

//        LazyVerticalStaggeredGrid(
//            columns = StaggeredGridCells.Fixed(10),
//            verticalItemSpacing = 6.dp,
//            horizontalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//
//            items(keyboard){
//
//                KeyboardButton(
//                    char = it.char,
//                    onClick = { charClicked ->
//                        onButtonClick(charClicked)
//                    },
//                    type = it.type
//                )
//
//            }
//
//        }

        Row(modifier = Modifier.fillMaxWidth().height(45.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            (0..10).forEach { index ->
                KeyboardButton(
                    modifier = Modifier.width(screenWidth / 11 - 4.dp),
                    char = keyboard[index].char,
                    onClick = { charClicked ->
                        onButtonClick(charClicked)
                    },
                    type = keyboard[index].type
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth().height(45.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            (10..20).forEach { index ->
                KeyboardButton(
                    modifier = Modifier.width(screenWidth / 11 - 4.dp),
                    char = keyboard[index].char,
                    onClick = { charClicked ->
                        onButtonClick(charClicked)
                    },
                    type = keyboard[index].type
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth().height(45.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            ActionKeyboardButton(
                modifier = Modifier.weight(1f),
                icon = R.drawable.baseline_send_24,
                onClick = {
                    onAcceptClick()
                },
                type = onAcceptState
            )
            (20..keyboard.lastIndex).forEach { index ->
                KeyboardButton(
                    modifier = Modifier.width(screenWidth / 11 - 4.dp),
                    char = keyboard[index].char,
                    onClick = { charClicked ->
                        onButtonClick(charClicked)
                    },
                    type = keyboard[index].type
                )
            }
            ActionKeyboardButton(
                modifier = Modifier.weight(1f),
                icon = R.drawable.baseline_backspace_24,
                onClick = {
                    onBackspaceClick()
                }
            )
        }

    }

}

data class KeyboardChar(
    val char: String,
    val type: ButtonType = ButtonType.Unclicked
)