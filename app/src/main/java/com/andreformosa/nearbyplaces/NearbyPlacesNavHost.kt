package com.andreformosa.nearbyplaces

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.andreformosa.nearbyplaces.Destinations.HOME_ROUTE
import com.andreformosa.nearbyplaces.feature.home.HomeScreen

object Destinations {
    const val HOME_ROUTE = "home"
}

@Composable
fun NearbyPlacesNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = HOME_ROUTE) {
        composable(HOME_ROUTE) {
            HomeScreen()
        }
    }
}
