package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.window.Dialog
import com.carlostorres.wordsgame.game.presentation.easy.HintType
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray

@Composable
fun BuyHintDialog(
    hintType: HintType,
    onDismiss: () -> Unit,
    onAccept: (HintType) -> Unit,
) {

    val hintCoast = when (hintType) {
        HintType.ONE_LETTER -> 75
        HintType.KEYBOARD -> 50
    }

    val dialogText = when (hintType) {
        HintType.ONE_LETTER -> "Compra una letra de la palabra secreta por 75 pejecoins"
        HintType.KEYBOARD -> "Descarta 3 letras del teclado por 50 pejecoins"
    }
    
    Dialog(onDismissRequest = {onDismiss()}) {

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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
                        text = dialogText,
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    MyButton(
                        modifier = Modifier,
                        onClick = {
                            when (hintType) {
                                HintType.ONE_LETTER -> onAccept(HintType.ONE_LETTER)
                                HintType.KEYBOARD -> onAccept(HintType.KEYBOARD)
                            }
                        },
                        difficult = GameDifficult.Easy,
                        text = "Aceptar"
                    )

                    MyButton(
                        modifier = Modifier,
                        onClick = {
                            onDismiss()
                        },
                        difficult = GameDifficult.Normal,
                        text = "Cancelar"
                    )

                }
            }
        }

    }

}