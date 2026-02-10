package com.example.miniapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.miniapp.data.repository.AuthRepository
import com.example.miniapp.ui.dashboard.DashboardScreen
import com.example.miniapp.ui.dashboard.DashboardViewModel
import com.example.miniapp.ui.login.LoginScreen
import com.example.miniapp.ui.login.LoginViewModel
import com.example.miniapp.ui.register.RegisterScreen
import com.example.miniapp.ui.register.RegisterViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    repository: AuthRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            val viewModel = LoginViewModel(repository)
            LoginScreen(
                onLoginSuccess = {
                    // Navigate to dashboard with user name from state
                    val userName = (viewModel.uiState.value as? com.example.miniapp.ui.login.LoginUiState.Success)?.userName ?: "User"
                    navController.navigate(Screen.Dashboard.createRoute(userName)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                viewModel = viewModel
            )
        }

        composable(Screen.Register.route) {
            val viewModel = RegisterViewModel(repository)
            RegisterScreen(
                onRegisterSuccess = {
                    // Navigate to dashboard with user name from state
                    val userName = (viewModel.uiState.value as? com.example.miniapp.ui.register.RegisterUiState.Success)?.userName ?: "User"
                    navController.navigate(Screen.Dashboard.createRoute(userName)) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.Dashboard.route,
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: "User"
            val viewModel = DashboardViewModel(repository)
            DashboardScreen(
                userName = userName,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
    }
}
