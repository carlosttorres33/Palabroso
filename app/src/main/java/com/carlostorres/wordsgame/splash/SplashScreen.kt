package com.carlostorres.wordsgame.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.carlostorres.wordsgame.R
import com.carlostorres.wordsgame.ui.navigation.NavRoutes
import com.carlostorres.wordsgame.ui.theme.DarkBackgroundGray
import com.carlostorres.wordsgame.ui.theme.DarkTextGray
import com.carlostorres.wordsgame.ui.theme.LightBackgroundGray
import com.carlostorres.wordsgame.utils.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val onBoardingCompleted by viewModel.onBoardingCompleted.collectAsState()


    LaunchedEffect(key1 = true) {
        delay(3000)
        navController.popBackStack()
        if (onBoardingCompleted) {
            navController.navigate(NavRoutes.Menu.route)
        } else {
            navController.navigate(NavRoutes.Onboarding.createRoute(false))
        }
    }

    Splash()

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Splash() {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (isSystemInDarkTheme()) DarkBackgroundGray else LightBackgroundGray
    ) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            val (
                topContainer,
                authorText
            ) = createRefs()

            val middleGuideline = createGuidelineFromTop(0.5f)

            BoxWithConstraints(
                modifier = Modifier
                    .constrainAs(topContainer){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentAlignment = Alignment.Center
            ) {

                val width = maxWidth
                val height = maxHeight

                Image(
                    painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.logo_dark_theme else R.drawable.logo),
                    contentDescription = "",
                    modifier = Modifier
                        .height(height / 2)
                        .width(width / 2),
                    contentScale = ContentScale.FillWidth,
                )

            }

            Text(
                modifier = Modifier
                    .constrainAs(authorText) {
                        top.linkTo(middleGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                text = "Desarrollado por Carlos Torres",
                color = if (isSystemInDarkTheme()) LightBackgroundGray else DarkTextGray,
                fontWeight = FontWeight.Bold
            )

        }

    }

}