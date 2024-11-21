package com.carlostorres.wordsgame.ui.navigation

sealed class NavRoutes (val route: String) {

    object Home : NavRoutes(route = "Home")
    object Menu : NavRoutes(route = "Menu")

}