package com.carlostorres.wordsgame.home.ui.components.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkCustomGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray

@Composable
fun GameLoseDialog(
    secretWord: String,
    onButtonClick: () -> Unit
) {

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Perdiste :c",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                )
                Text(
                    text = "La palabra era: $secretWord",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                )

                Button(
                    modifier = Modifier
                        .bounceClick(),
                    onClick = {
                       onButtonClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color.Black else DarkCustomGray
                    )
                ) {
                    Text(
                        text = "Jugar de nuevo",
                        color = if (isSystemInDarkTheme()) Color.White else Color.White
                    )
                }

            }
        }
    }


}