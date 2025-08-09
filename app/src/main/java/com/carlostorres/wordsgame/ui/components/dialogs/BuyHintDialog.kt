package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.Dimension
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.CornerButton
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightYellow
import com.carlostorres.wordsgame.ui.theme.ROUND_CORNER_SIZE
import com.carlostorres.wordsgame.utils.ButtonPlace
import com.carlostorres.wordsgame.utils.HintType

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
                ),
                shape = RoundedCornerShape(ROUND_CORNER_SIZE.dp)
            ) {

                BuyHintContent(
                    modifier = Modifier
                        .fillMaxWidth(),
                    dialogText = dialogText,
                    hintType = hintType,
                    onDismiss = onDismiss,
                    onAccept = {onAccept(it)}
                )

            }
        }

    }

}

@Composable
fun BuyHintContent(
    modifier: Modifier = Modifier,
    dialogText: String,
    hintType: HintType,
    onDismiss: () -> Unit,
    onAccept: (HintType) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(top = 32.dp, start = 32.dp, end = 32.dp),
            text = dialogText,
            color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            CornerButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                buttonPlace = ButtonPlace.BottomStart,
                buttonText = "Aceptar",
                textColor = if (isSystemInDarkTheme()) DarkGreen else LightGreen
            ) {
                when (hintType) {
                    HintType.ONE_LETTER -> onAccept(HintType.ONE_LETTER)
                    HintType.KEYBOARD -> onAccept(HintType.KEYBOARD)
                }
            }

            VerticalDivider(modifier = Modifier.fillMaxHeight())

            CornerButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                buttonPlace = ButtonPlace.BottomEnd,
                buttonText = "Cancelar",
                textColor = DarkYellow
            ) {
                onDismiss()
            }

        }

    }

}

@Composable
fun BuyHintContentBS(
    modifier: Modifier = Modifier,
    dialogText: String,
    hintType: HintType,
    onDismiss: (HintType) -> Unit,
    onAccept: (HintType) -> Unit
) {

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

@Preview
@Composable
private fun PBHD() {
    BuyHintContentBS(
        modifier = Modifier.background(Color.White),
        hintType = HintType.KEYBOARD,
        dialogText = "Compra una letra de la palabra secreta por 75 pejecoins",
        onDismiss = {  }
    ) {

    }
}