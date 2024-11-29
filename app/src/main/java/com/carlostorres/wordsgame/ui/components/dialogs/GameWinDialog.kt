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
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
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
                        text = "GANASTE",
                        color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
                        fontWeight = FontWeight.Bold
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
                            modifier = Modifier,
                            onClick = {
                                onRematchClick()
                            },
                            difficult = GameDifficult.Easy,
                            text = "Jugar de nuevo"
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