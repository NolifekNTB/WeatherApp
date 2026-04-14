package com.example.weatherapptask.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapptask.domain.model.CitySuggestion
import com.example.weatherapptask.ui.search.SearchScreen
import com.example.weatherapptask.ui.details.DetailsScreen

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object Details : Screen("details/{cityKey}/{cityName}/{admin}/{country}") {
        fun createRoute(city: CitySuggestion): String {
            return "details/${city.key}/${Uri.encode(city.name)}/${Uri.encode(city.administrativeArea)}/${Uri.encode(city.country)}"
        }
    }
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Search.route
    ) {
        composable(Screen.Search.route) {
            SearchScreen(
                onCityClick = { city ->
                    navController.navigate(Screen.Details.createRoute(city))
                }
            )
        }
        composable(Screen.Details.route) {
            DetailsScreen(onBack = navController::navigateUp)
        }
    }
}
