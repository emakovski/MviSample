package com.makovsky.mvi

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.makovsky.mvi.navigation.Route
import com.makovsky.mvi.presentation.details.DetailsScreen
import com.makovsky.mvi.presentation.details.DetailsViewModel
import com.makovsky.mvi.presentation.main.MainScreen
import com.makovsky.mvi.presentation.main.MainViewModel
import com.makovsky.mvi.ui.theme.MviSampleTheme

@Composable
fun MviApp() {
    val navController = rememberNavController()
    MviSampleTheme {
        Scaffold(
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Route.route,
                    modifier = Modifier.padding(padding)
                ) {
                    mainGraph(
                        navBack = { navController.popBackStack() },
                        navToPokemon = { pokemon -> navController.navigate(Route.detail(pokemon)) }
                    )
                }
            }
        )
    }
}

private fun NavGraphBuilder.mainGraph(
    navBack: () -> Unit,
    navToPokemon: (String) -> Unit,
) {
    navigation(
        startDestination = Route.defaultDestination,
        route = Route.route
    ) {
        composable(Route.defaultDestination) {
            val viewModel = hiltViewModel<MainViewModel>()
            MainScreen(
                viewModel = viewModel,
                navToPokemon = navToPokemon
            )
        }
        composable(
            Route.detail,
            arguments = listOf(navArgument("pokemon") {
                type = NavType.StringType
            })
        ) { entry ->
            val pokemon = entry.arguments?.getString("pokemon") ?: ""
            val viewModel: DetailsViewModel = hiltViewModel()
            DetailsScreen(
                pokemon = pokemon,
                viewModel = viewModel,
                navBack = navBack,
            )
        }
    }
}