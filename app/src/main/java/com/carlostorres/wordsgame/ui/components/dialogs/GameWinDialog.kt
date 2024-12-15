package com.carlostorres.wordsgame.ui.components.dialogs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun GameWinDialog(
    onRematchClick: () -> Unit,
    onHomeClick: () -> Unit,
    isGameLimitReached: Boolean
) {
    Dialog(onDismissRequest = {}) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        text = "GANASTE",
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        textAlign = TextAlign.Center,
                        text = "Has obtenido 25 pejecoins",
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isGameLimitReached) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Ya jugaste todas las palabras de hoy",
                            color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    } else {

                        HorizontalDivider()

                        CornerButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            buttonPlace = ButtonPlace.Center,
                            buttonText = "Jugar de nuevo",
                            textColor = if (isSystemInDarkTheme()) DarkGreen else LightGreen
                        ) {
                            onRematchClick()
                        }

                    }

                    HorizontalDivider()

                    CornerButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        buttonPlace = ButtonPlace.Bottom,
                        buttonText = "Volver al inicio",
                        textColor = if (isSystemInDarkTheme()) DarkYellow else LightYellow
                    ) {
                        onHomeClick()
                    }

                }
            }
        }

        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(
                Party(
                    emitter = Emitter(duration = 4, TimeUnit.SECONDS).perSecond(30)
                ),
                Party(
                    emitter = Emitter(duration = 4, TimeUnit.SECONDS).perSecond(30)
                ),
                Party(
                    emitter = Emitter(duration = 4, TimeUnit.SECONDS).perSecond(30)
                )
            )
        )
    }
}

@Preview
@Composable
private fun GameWinDialogPreview() {
    GameWinDialog(onRematchClick = { /*TODO*/ }, onHomeClick = { /*TODO*/ }, isGameLimitReached = false)
}