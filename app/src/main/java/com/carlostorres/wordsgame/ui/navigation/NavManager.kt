package com.carlostorres.wordsgame.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlostorres.wordsgame.home.ui.EasyScreen
import com.carlostorres.wordsgame.menu.ui.MenuScreen
import com.carlostorres.wordsgame.ui.components.GameDifficult

@Composable
fun NavManager() {

    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Menu.route,
    ) {

        composable(
            route = NavRoutes.Home.route,
        ) {
            EasyScreen()
        }

        composable(
            route = NavRoutes.Menu.route,
        ) {
            MenuScreen(
                onDifficultySelected = { difficult ->
                    when (difficult) {
                        GameDifficult.Easy -> {
                            Toast.makeText(context, "Facil", Toast.LENGTH_SHORT).show()
                        }
                        GameDifficult.Medium -> {
                            navController.navigate(NavRoutes.Home.route)
                        }
                        GameDifficult.Hard -> {
                            Toast.makeText(context, "Dificil", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            )
        }

    }

}