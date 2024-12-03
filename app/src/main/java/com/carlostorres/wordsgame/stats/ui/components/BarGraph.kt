package com.carlostorres.wordsgame.stats.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carlostorres.wordsgame.ui.theme.LightGreen
import com.carlostorres.wordsgame.ui.theme.LightRed

@Composable
fun CustomChart(
    modifier: Modifier = Modifier,
    barValue: List<Int>,
    xAxisScale: List<String>,
    total_amount: Int
) {
    val context = LocalContext.current
    // BarGraph Dimensions
    val barGraphHeight by remember { mutableStateOf(200.dp) }
    val barGraphWidth by remember { mutableStateOf(20.dp) }
    // Scale Dimensions
    val scaleYAxisWidth by remember { mutableStateOf(50.dp) }
    val scaleLineWidth by remember { mutableStateOf(2.dp) }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            // scale Y-Axis
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleYAxisWidth),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedContent(targetState = total_amount) { totalAmountAnim ->

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(text = totalAmountAnim.toString())
                        Spacer(modifier = Modifier.fillMaxHeight())
                    }

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(text = (totalAmountAnim / 2).toString())
                        Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                    }

                }

            }

            // Y-Axis Line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleLineWidth)
                    .background(Color.Black)
            )

            // graph
            barValue.forEachIndexed { index, value ->

                val colorBar = if (index == 5) LightRed else LightGreen

                var animationTriggered by remember {
                    mutableStateOf(false)
                }

                val graphBarHeight by animateFloatAsState(
                    targetValue = if (animationTriggered) value.toFloat() / total_amount.toFloat() else 0f,
                    animationSpec = tween(
                        durationMillis = 1000,
                        delayMillis = 0
                    )
                )

                LaunchedEffect(key1 = true) {
                    animationTriggered = true
                }

                Box(
                    modifier = Modifier
                        .padding(start = barGraphWidth, bottom = 5.dp)
                        .clip(CircleShape)
                        .width(barGraphWidth)
                        .fillMaxHeight(graphBarHeight)
                        .background(colorBar)
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    "${index + 1}: $value",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                )
            }

        }

        // X-Axis Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(scaleLineWidth)
                .background(Color.Black)
        )

        // Scale X-Axis
        Box(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text(text = "Intentos")
            
            Row(
                modifier = Modifier
                    .padding(start = scaleYAxisWidth + barGraphWidth + scaleLineWidth)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(barGraphWidth)
            ) {

                xAxisScale.forEach {
                    Text(
                        modifier = Modifier.width(barGraphWidth),
                        text = it,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }

    }

}