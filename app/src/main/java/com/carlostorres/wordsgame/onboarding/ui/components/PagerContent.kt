package com.carlostorres.wordsgame.onboarding.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.game.data.model.InstructionsPages
import com.carlostorres.wordsgame.ui.components.HintBox
import com.carlostorres.wordsgame.ui.components.keyboard.ActionKeyboardButton
import com.carlostorres.wordsgame.ui.components.keyboard.KeyboardButton
import com.carlostorres.wordsgame.ui.components.word_line.WordChar
import com.carlostorres.wordsgame.ui.components.word_line.WordCharState
import com.carlostorres.wordsgame.ui.theme.DarkCustomGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightCustomGray
import com.carlostorres.wordsgame.utils.Constants.KEYBOARD_HINT_PRICE
import com.carlostorres.wordsgame.utils.Constants.ONE_LETTER_HINT_PRICE
import com.carlostorres.wordsgame.utils.HintType

@Composable
fun PagerContent(
    modifier: Modifier = Modifier,
    page: InstructionsPages,
    index: Int
) {

    val textColor = if (isSystemInDarkTheme()) LightCustomGray else Color.Black

    Column(
        modifier = modifier
            .padding(start = 32.dp, end = 32.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = page.title,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (index) {
            0 -> {
                Text(
                    text = "1. Ingresa una palabra de 5 letras utilizando el teclado en pantalla",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "2. Presiona \"➤\" para enviar tu intento",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "3. Observa los colores de las letras:",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = DarkCustomGray)
                        ) { append("Gris") }
                        append(": La letra no está en la palabra secreta")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    color = textColor
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = DarkYellow)
                        ) { append("Amarillo") }
                        append(": La letra está en la palabra secreta, pero en una posición diferente")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    color = textColor
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = DarkGreen)
                        ) { append("Verde") }
                        append(": La letra está en la palabra secreta y en la posición correcta")
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "4. Usa las pistas de colores para ajustar tus siguientes intentos",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "5. Continúa adivinando hasta que encuentres la palabra secreta o agotes tus 5 intentos",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
            }

            1 -> {
                Text(
                    text = "Si la palabra secreta es \"LLAVE\" y tu intento es \"FUEGO\":",
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = buildAnnotatedString {
                        append("La \"F\", \"U\", \"G\" y \"O\" se colorearán de ")
                        withStyle(
                            style = SpanStyle(color = DarkCustomGray)
                        ) { append("gris") }
                        append(" porque no están en \"LLAVE\"")
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = buildAnnotatedString {
                        append("La \"E\" se colorearán de ")
                        withStyle(
                            style = SpanStyle(color = DarkYellow)
                        ) { append("amarillo") }
                        append(" porque está en \"LLAVE\", pero en una posición diferente")
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = buildAnnotatedString {
                        append("Si intentas con \"CLASE\", la \"L\", \"A\" y \"E\" se colorearán de ")
                        withStyle(
                            style = SpanStyle(color = DarkGreen)
                        ) { append("verde") }
                        append(" porque están en \"LLAVE\" y en su posición correcta")
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    color = textColor
                )
            }

            2 -> {
                Text(
                    text = "    Comienza con palabras que contengan vocales comunes (A, E, I, O, U)",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "    Presta atención a las letras que ya has adivinado y sus colores",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "    Intenta usar letras nuevas en cada intento para obtener más información",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "    Compra pistas usando los botones para obtener una letra de la palabra secreta o descartar algunas letras del teclado",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "¡Piensa estratégicamente y diviértete!",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }

            3 -> {
                Text(
                    text = "    Selecciona el modo que desees jugar en el manú principal (4, 5 o 6 Letras)",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "    Cada día tendras la posibilidad de adivinar 5 palabras diferentes de cada modo",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
                Text(
                    text = "    Recuerda que cada palabra que adivines te dará más Pejecoins para poder comprar pistas y continuar tu racha",
                    modifier = Modifier.fillMaxWidth(),
                    color = textColor
                )
            }

            else -> {}
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (index) {
                0 -> {
                    ActionKeyboardButton(
                        icon = R.drawable.baseline_send_24,
                        onClick = { },
                        modifier = Modifier.size(40.dp)
                    )
                }
                1 -> {
                    WordChar(char = 'A', charState =WordCharState.IsOnWord, modifier = Modifier.size(60.dp))
                    WordChar(char = 'B', charState =WordCharState.IsNotInWord, modifier = Modifier.size(60.dp))
                    WordChar(char = 'C', charState =WordCharState.IsOnPosition, modifier = Modifier.size(60.dp))
                }
                2 -> {
                    HintBox(
                        modifier = Modifier.size(50.dp),
                        icon = R.drawable.text_magnifying_glass,
                        hintCoast = ONE_LETTER_HINT_PRICE,
                        clickEnabled = false
                    ) {}

                    HintBox(
                        modifier = Modifier.size(50.dp),
                        icon = R.drawable.packages,
                        hintCoast = KEYBOARD_HINT_PRICE,
                        clickEnabled = false
                    ) {}
                }
                else -> {}
            }

        }
            Spacer(modifier = Modifier.weight(1f))

    }

}