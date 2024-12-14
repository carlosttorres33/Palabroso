package com.carlostorres.wordsgame.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.bounceClick
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkYellow
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightCustomGray
import com.carlostorres.wordsgame.ui.theme.LightYellow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CoinsCounter(
    modifier: Modifier = Modifier,
    icon: Int,
    coinsRemaining: Int,
    clickEnabled: Boolean = true,
    clickAction: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .bounceClick()
            .clickable(clickEnabled) {
                clickAction()
            }
    ) {

        val (
            iconRef,
            hintsRemainingRef
        ) = createRefs()

        val verticalGuideline = createGuidelineFromStart(0.5f)
        val verticalGuidelineEnd = createGuidelineFromStart(0.95f)
        val horizontalGuideline = createGuidelineFromTop(0.5f)
        val horizontalGuidelineTop = createGuidelineFromTop(0.1f)

        Card(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .constrainAs(iconRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                },
            shape = RoundedCornerShape(18.dp),
            backgroundColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray,
            border = BorderStroke(0.dp, Color.Transparent),
            elevation = 0.dp
        ) {

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {

                val width = maxWidth
                val height = maxHeight
                val iconHeight = (height / 4) * 3

                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .height(iconHeight)
                            .aspectRatio(1f)
                            .padding(2.dp),
                        painter = painterResource(id = icon),
                        contentDescription = "",
                        tint = if (isSystemInDarkTheme()) DarkYellow else LightYellow
                    )

                    Text(
                        text = coinsRemaining.toString(),
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Start
                    )

                    Icon(
                        modifier = Modifier
                            .size(height/2),
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "",
                        tint = if (isSystemInDarkTheme()) LightBackgroundGray else DarkBackgroundGray
                    )

                    Spacer(modifier = Modifier.width(5.dp))
                }

            }

        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
private fun CoinsCounterPrev() {
    CenterAlignedTopAppBar(
        title = { Text(text = "PALABROSO") },
        actions = {
        CoinsCounter(
            icon = R.drawable.coins, coinsRemaining = 250, modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
        ) {
        }
    })
}