package com.carlostorres.wordsgame.ui.components.dialogs

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightRed

@Composable
fun GameErrorDialog(
    textError: String,
    onRetryClick: () -> Unit,
    onHomeClick: () -> Unit
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
                    text = "Hubo un error al iniciar el juego :c",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = textError,
                    color = if (isSystemInDarkTheme()) DarkRed else LightRed,
                    textAlign = TextAlign.Center
                )

                MyButton(
                    modifier = Modifier
                        .bounceClick(),
                    onClick = {
                        onRetryClick()
                    },
                    difficult = GameDifficult.Easy,
                    text = "Reintentar"
                )

                MyButton(
                    modifier = Modifier
                        .bounceClick(),
                    onClick = {
                        onHomeClick()
                    },
                    difficult = GameDifficult.Hard,
                    text = "Volver al inicio"
                )

            }
        }
    }


}