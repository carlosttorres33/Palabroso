package com.carlostorres.wordsgame.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlostorres.wordsgame.home.ui.HomeRebuild
import com.carlostorres.wordsgame.home.ui.HomeScreen

@Composable
fun NavManager() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
    ) {

        composable(
            route = NavRoutes.Home.route,
        ){

            HomeScreen()
            //HomeRebuild()
        }

    }

}