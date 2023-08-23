package com.ps.happydays.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNavGraph(
    startDestinationRoute: String, navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        authenticationRoute()
        homeRoute()
        writeRoute()
    }

}


fun NavGraphBuilder.authenticationRoute() {
    composable(route = Screen.Authentication.route) {

    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Authentication.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Authentication.route,
        arguments = listOf(navArgument(name = NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}