package com.carlostorres.wordsgame.ui.navigation

sealed class NavRoutes (val route: String) {

    object NormalGame : NavRoutes(route = "NormalGame")
    object EasyGame : NavRoutes(route = "EasyGame")
    object HardGame : NavRoutes(route = "HardGame")
    object Menu : NavRoutes(route = "Menu")
    object Onboarding : NavRoutes(route = "Onboarding/{isFromMenu}"){
        fun createRoute(isFromMenu : Boolean) : String {
            return "Onboarding/$isFromMenu"
        }
    }
    object Splash : NavRoutes(route = "Splash")
    object Stats : NavRoutes(route = "Stats")

}