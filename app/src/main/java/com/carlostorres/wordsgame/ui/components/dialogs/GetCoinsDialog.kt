package com.carlostorres.wordsgame.ui.components.dialogs

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
fun GetCoinsDialog(
    onAcceptClick: () -> Unit,
    onCancelClick: () -> Unit,
) {

    Dialog(onDismissRequest = {onCancelClick()}) {

        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
                ),
                shape = RoundedCornerShape(ROUND_CORNER_SIZE.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(top = 32.dp),
                        text = "Deseas ver un anucnio para obtener 75 Pejecoins?",
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Icon(
                        modifier = Modifier
                            .size(100.dp),
                        painter = painterResource(id = R.drawable.coins),
                        contentDescription = "",
                        tint = if (isSystemInDarkTheme()) DarkYellow else LightYellow
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
                            buttonText = "Aceptar",
                            textColor = if (isSystemInDarkTheme()) DarkGreen else LightGreen
                        ) {
                            onAcceptClick()
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
                            onCancelClick()
                        }

                    }

                }
            }
        }
    }

}