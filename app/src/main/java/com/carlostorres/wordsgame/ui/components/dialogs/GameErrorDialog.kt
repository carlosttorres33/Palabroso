package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.components.CornerButton
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkRed
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed
import com.carlostorres.wordsgame.utils.ButtonPlace
import com.carlostorres.wordsgame.utils.HintType

@Composable
fun GameErrorDialog(
    textError: String,
    onRetryClick: () -> Unit,
    onHomeClick: () -> Unit
) {

    Dialog(onDismissRequest = {}, properties = DialogProperties(dismissOnBackPress = false)) {
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

                Text(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .padding(horizontal = 32.dp),
                    text = "Hubo un error al iniciar el juego :c",
                    color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    text = textError,
                    color = if (isSystemInDarkTheme()) DarkRed else LightRed,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                        buttonText = "Reintentar",
                        textColor = if (isSystemInDarkTheme()) DarkGreen else LightGreen
                    ) {
                        onRetryClick()
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
                        onHomeClick()
                    }

                }

            }
        }
    }


}

@Preview
@Composable
private fun GameErrorDialogPreview() {
    GameErrorDialog(textError = "404 Lorem ipsum dolor sit amet and so on the ", onRetryClick = { /*TODO*/ }) {
        
    }
}