package com.carlostorres.wordsgame.home.ui.components.instructions_dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.home.data.model.InstructionsPages
import com.carlostorres.wordsgame.ui.theme.DarkCustomGray
import com.carlostorres.wordsgame.ui.theme.DarkGreen
import com.carlostorres.wordsgame.ui.theme.DarkYellow

@Composable
fun PagerContent(
    modifier: Modifier = Modifier,
    page: InstructionsPages,
    index: Int
) {

    Column(
        modifier = modifier,
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

        when (index) {
            0 -> {
                Text(
                    text = "1. Ingresa una palabra de 5 letras utilizando el teclado en pantalla.",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "2. Presiona \"➤\" para enviar tu intento.",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "3. Observa los colores de las letras:",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = DarkCustomGray)
                        ) { append("Gris") }
                        append(": La letra no está en la palabra secreta.")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = DarkYellow)
                        ) { append("Amarillo") }
                        append(": La letra está en la palabra secreta, pero en una posición diferente.")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = DarkGreen)
                        ) { append("Verde") }
                        append(": La letra está en la palabra secreta y en la posición correcta.")
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = "4. Usa las pistas de colores para ajustar tus siguientes intentos.",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "5. Continúa adivinando hasta que encuentres la palabra secreta o agotes tus 5 intentos.",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            1 -> {
                Text(
                    text = "Si la palabra secreta es \"LLAVE\" y tu intento es \"FUEGO\":",
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = buildAnnotatedString {
                        append("La \"F\", \"U\", \"G\" y \"O\" se colorearán de ")
                        withStyle(
                            style = SpanStyle(color = DarkCustomGray)
                        ) { append("gris") }
                        append(" porque no están en \"LLAVE\".")
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
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
                        .fillMaxWidth()
                )
                Text(
                    text = buildAnnotatedString {
                        append("Si intentas con \"CLASE\", la \"L\", \"A\" y \"E\" se colorearán de ")
                        withStyle(
                            style = SpanStyle(color = DarkGreen)
                        ) { append("verde") }
                        append(" porque están en \"LLAVE\" y en su posición correcta.")
                    },
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                )
            }

            2 -> {
                Text(
                    text = "Comienza con palabras que contengan vocales comunes (A, E, I, O, U).",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Presta atención a las letras que ya has adivinado y sus colores.",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Intenta usar letras nuevas en cada intento para obtener más información.",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "¡Piensa estratégicamente y diviértete!",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            else -> {}
        }

//        if (page.image != null) {
//            Image(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .aspectRatio(1f),
//                painter = painterResource(id = page.image),
//                contentDescription = "",
//                contentScale = ContentScale.Inside
//            )
//        }

    }

}