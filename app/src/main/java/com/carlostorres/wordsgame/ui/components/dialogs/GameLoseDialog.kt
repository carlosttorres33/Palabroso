package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.carlostorres.wordsgame.ui.components.CornerButton
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.utils.ButtonPlace

@Composable
fun GameLoseDialog(
    secretWord: String,
    onRetryClick: () -> Unit,
    onHomeClick: () -> Unit,
    isGameLimitReached: Boolean,
    reportWordEnabled: Boolean = true,
    onReportWordClick: () -> Unit = {},
    isConnected: Boolean = true
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
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (reportWordEnabled) {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            modifier = Modifier
                                .padding(end = 8.dp, top = 3.dp)
                                .clickable(enabled = isConnected) {
                                    onReportWordClick()
                                },
                            text = "¡Reportar palabra!",
                            color = if (isSystemInDarkTheme()) DarkRed else LightRed,
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = 32.dp),
                    textAlign = TextAlign.Center,
                    text = "Perdiste :c",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    textAlign = TextAlign.Center,
                    text = "La palabra era: ${secretWord.uppercase()}",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isGameLimitReached) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        text = "Ya jugaste todas las palabras de hoy",
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {

                    HorizontalDivider()

                    CornerButton(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        buttonPlace = ButtonPlace.Center,
                        buttonText = "Reintentar",
                        textColor = if (isSystemInDarkTheme()) DarkGreen else LightGreen
                    ) {
                        onRetryClick()
                    }

                }

                HorizontalDivider()

                CornerButton(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    buttonPlace = ButtonPlace.Center,
                    buttonText = "Volver al Inicio",
                    textColor = if (isSystemInDarkTheme()) DarkRed else LightRed
                ) {
                    onHomeClick()
                }

            }
        }
    }


}