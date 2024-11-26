package com.carlostorres.wordsgame.ui.components.keyboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
    ) {

        val maxWidth = this.maxWidth
        val buttonWidth = maxWidth / 10 - 3.dp

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (0..9).forEach { index ->
                    KeyboardButton(
                        modifier = Modifier
                            .width(buttonWidth)
                            .fillMaxHeight(),
                        char = keyboard[index].char,
                        onClick = { charClicked ->
                            onButtonClick(charClicked)
                        },
                        type = keyboard[index].type
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (10..19).forEach { index ->
                    KeyboardButton(
                        modifier = Modifier
                            .width(buttonWidth)
                            .fillMaxHeight(),
                        char = keyboard[index].char,
                        onClick = { charClicked ->
                            onButtonClick(charClicked)
                        },
                        type = keyboard[index].type
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionKeyboardButton(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = R.drawable.baseline_send_24,
                    onClick = {
                        onAcceptClick()
                    },
                    type = onAcceptState
                )
                Row(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement
                        .spacedBy(3.dp)
                ) {
                    (20..keyboard.lastIndex).forEach { index ->
                        KeyboardButton(
                            modifier = Modifier
                                .width(buttonWidth)
                                .fillMaxHeight(),
                            char = keyboard[index].char,
                            onClick = { charClicked ->
                                onButtonClick(charClicked)
                            },
                            type = keyboard[index].type
                        )
                    }
                }
                ActionKeyboardButton(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    icon = R.drawable.baseline_backspace_24,
                    onClick = {
                        onBackspaceClick()
                    }
                )
            }
        }
    }
}

data class KeyboardChar(
    val char: String,
    val type: ButtonType = ButtonType.Unclicked
)