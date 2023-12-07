package com.makovsky.mvi

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.makovsky.mvi.navigation.Route
import com.makovsky.mvi.presentation.main.MainScreen
import com.makovsky.mvi.presentation.main.MainViewModel
import com.makovsky.mvi.ui.theme.MviSampleTheme

@Composable
fun MviApp() {
    val navController = rememberNavController()
    MviSampleTheme {
        Scaffold(
            content = {
                NavHost(navController = navController, startDestination = Route.MainScreen) {
                    mainScreenRoute(navController = navController)
                }
            }
        )
    }
}

private fun NavGraphBuilder.mainScreenRoute(navController: NavController) {
    composable(Route.MainScreen) {
        val viewModel = hiltViewModel<MainViewModel>()
        MainScreen(viewModel = viewModel)
    }
}