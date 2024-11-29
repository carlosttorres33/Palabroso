package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray

@Composable
fun GameLoseDialog(
    secretWord: String,
    onRetryClick: () -> Unit,
    onHomeClick: () -> Unit,
    isGameLimitReached: Boolean
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
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "La palabra era: ${secretWord.uppercase()}",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                )

                if (isGameLimitReached) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Ya jugaste todas las palabras de hoy",
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {

                    MyButton(
                        modifier = Modifier
                            .bounceClick(),
                        onClick = {
                            onRetryClick()
                        },
                        difficult = GameDifficult.Easy,
                        text = "Reintentar"
                    )

                }

                MyButton(
                    modifier = Modifier,
                    onClick = {
                        onHomeClick()
                    },
                    difficult = GameDifficult.Normal,
                    text = "Volver al inicio"
                )

            }
        }
    }


}