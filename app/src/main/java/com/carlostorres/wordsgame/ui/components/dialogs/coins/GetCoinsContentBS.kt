package com.carlostorres.wordsgame.ui.components.dialogs.coins

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.components.BannerAd
import com.carlostorres.wordsgame.ui.components.GameDifficult
import com.carlostorres.wordsgame.ui.components.MyButton
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightYellow

@Composable
fun GetCoinsContentBS(
    modifier: Modifier = Modifier,
    onAcceptClick: () -> Unit,
    onCancelClick: () -> Unit,
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
            text = "Deseas ver un anucnio para obtener 75 Pejecoins?",
            color = if (isSystemInDarkTheme()) DarkTextGray else Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Icon(
            modifier = Modifier
                .size(75.dp),
            painter = painterResource(id = R.drawable.coins),
            contentDescription = "",
            tint = if (isSystemInDarkTheme()) DarkYellow else LightYellow
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
                    .weight(1f)
                    .fillMaxHeight(),
                text = "Aceptar",
                difficult = GameDifficult.Easy
            ) {
                onAcceptClick()
            }

            MyButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                text = "Cancelar",
                difficult = GameDifficult.Normal
            ) {
                onCancelClick()
            }

        }

        BannerAd(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )

    }

}