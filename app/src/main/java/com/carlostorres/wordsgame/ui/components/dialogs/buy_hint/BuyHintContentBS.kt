package com.carlostorres.wordsgame.ui.components.dialogs.buy_hint

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.utils.HintType


@Composable
fun BuyHintContentBS(
    modifier: Modifier = Modifier,
    hintType: HintType,
    onDismiss: (HintType) -> Unit,
    onAccept: (HintType) -> Unit
) {

    val hintCoast = when (hintType) {
        HintType.ONE_LETTER -> 75
        HintType.KEYBOARD -> 50
    }

    val dialogText = when (hintType) {
        HintType.ONE_LETTER -> "Compra una letra de la palabra secreta por $hintCoast pejecoins"
        HintType.KEYBOARD -> "Descarta 3 letras del teclado por $hintCoast pejecoins"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        Text(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            text = dialogText,
            color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            MyButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                difficult = GameDifficult.Easy,
                text = "Aceptar"
            ) {
                when (hintType) {
                    HintType.ONE_LETTER -> onAccept(HintType.ONE_LETTER)
                    HintType.KEYBOARD -> onAccept(HintType.KEYBOARD)
                }
            }

            MyButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                difficult = GameDifficult.Normal,
                text = "Cancelar"
            ) {
                onDismiss(hintType)
            }

        }

        BannerAd(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )

    }

}