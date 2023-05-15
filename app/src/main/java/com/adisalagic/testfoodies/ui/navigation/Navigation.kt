package com.adisalagic.testfoodies.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adisalagic.testfoodies.ui.routes.Home

@Composable
fun NavigationGraph(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavigationRoute.Home.route
    ) {
        composable(NavigationRoute.Home.route) {
            Home()
        }
        composable(NavigationRoute.Bucket.route) {

        }
        composable(NavigationRoute.Search.route) {

        }
    }
}