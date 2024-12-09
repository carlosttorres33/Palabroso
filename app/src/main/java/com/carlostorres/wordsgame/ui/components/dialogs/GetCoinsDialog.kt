package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightYellow

@Composable
fun GetCoinsDialog(
    onAcceptClick: () -> Unit,
    onCancelClick: () -> Unit,
) {

    Dialog(onDismissRequest = {onCancelClick()}) {

        Box(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
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
                        text = "Deseas ver un anucnio para obtener 75 Pejecoins?",
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        modifier = Modifier.size(100.dp),
                        painter = painterResource(id = R.drawable.coins),
                        contentDescription = "",
                        tint = if (isSystemInDarkTheme()) DarkYellow else LightYellow
                    )

                    MyButton(
                        modifier = Modifier,
                        onClick = {
                            onAcceptClick()
                        },
                        difficult = GameDifficult.Easy,
                        text = "Obtener Pejecoins"
                    )

                    MyButton(
                        modifier = Modifier,
                        onClick = {
                            onCancelClick()
                        },
                        difficult = GameDifficult.Normal,
                        text = "Cancelar"
                    )

                }
            }
        }
    }

}