package com.carlostorres.wordsgame.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.carlostorres.wordsgame.game.ui.EasyScreen
import com.carlostorres.wordsgame.game.ui.HardScreen
import com.carlostorres.wordsgame.game.ui.NormalScreen
import com.carlostorres.wordsgame.menu.ui.MenuScreen
import com.carlostorres.wordsgame.onboarding.ui.OnboardingScreen
import com.carlostorres.wordsgame.splash.SplashScreen
import com.carlostorres.wordsgame.ui.components.GameDifficult

@Composable
fun NavManager() {

    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash.route,
    ) {

        composable(
            route = NavRoutes.Splash.route
        ){
            SplashScreen(
                navController = navController
            )
        }

        composable(
            route = NavRoutes.Onboarding.route,
            arguments = listOf(
                navArgument("isFromMenu") {
                    type = NavType.BoolType
                }
            )
        ) {
            val isFromMenu = it.arguments?.getBoolean("isFromMenu") ?: false
            OnboardingScreen(
                onFinish = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.Menu.route)
                },
                onFinishMenu = {
                    navController.popBackStack()
                },
                isFromMenu = isFromMenu
            )
        }

        composable(
            route = NavRoutes.Menu.route,
        ) {
            MenuScreen(
                onDifficultySelected = { difficult ->
                    when (difficult) {
                        GameDifficult.Easy -> {
                            navController.navigate(NavRoutes.EasyGame.route)
                        }
                        GameDifficult.Normal -> {
                            navController.navigate(NavRoutes.NormalGame.route)
                        }
                        GameDifficult.Hard -> {
                            navController.navigate(NavRoutes.HardGame.route)

                        }
                    }
                },
                onHowToPlayClick = {
                    navController.navigate(NavRoutes.Onboarding.createRoute(true))
                }
            )
        }

        composable(
            route = NavRoutes.NormalGame.route,
        ) {
            NormalScreen(
                onHomeClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavRoutes.EasyGame.route,
        ){
            EasyScreen(
                onHomeClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavRoutes.HardGame.route,
        ){
            HardScreen(
                onHomeClick = {
                    navController.popBackStack()
                }
            )
        }

    }

}