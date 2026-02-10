package com.example.miniapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard/{userName}") {
        fun createRoute(userName: String) = "dashboard/$userName"
    }
}
