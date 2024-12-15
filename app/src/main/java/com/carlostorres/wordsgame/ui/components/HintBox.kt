package com.carlostorres.wordsgame.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.LightCustomGray

@Composable
fun HintBox(
    modifier: Modifier = Modifier,
    icon: Int,
    hintCoast: Int,
    clickEnabled: Boolean = true,
    clickAction: () -> Unit
) {

    ConstraintLayout(
        modifier = modifier
            .clickable(enabled = clickEnabled) { clickAction() }
    ) {

        val (
            iconRef,
            hintsRemainingRef
        ) = createRefs()

        val topToBottomGuideline = createGuidelineFromStart(0.5f)
        val startToEndGuideline = createGuidelineFromTop(0.5f)

        Icon(
            modifier = Modifier
                .constrainAs(iconRef){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }
                .aspectRatio(1f),
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = if (isSystemInDarkTheme()) LightCustomGray else DarkBackgroundGray
        )

//        Column(
//            modifier = Modifier
//                .constrainAs(hintsRemainingRef) {
//                    top.linkTo(parent.top)
//                    end.linkTo(parent.end)
//                    bottom.linkTo(startToEndGuideline)
//                    height = Dimension.fillToConstraints
//                    width = Dimension.ratio("1:1")
//                }
//                .background(
//                    color = Color.Blue.copy(alpha = 0.8f),
//                    shape = CircleShape
//                ),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            Text(modifier = Modifier.padding(horizontal = 3.dp), text = hintCoast.toString(), color = Color.White, fontSize = 10.sp)
//        }

    }

}

@Preview()
@Composable
private fun HintBoxPreview() {
    HintBox(
        modifier = Modifier.size(50.dp),
        icon = R.drawable.text_magnifying_glass,
        hintCoast = 3,
    ){}
}