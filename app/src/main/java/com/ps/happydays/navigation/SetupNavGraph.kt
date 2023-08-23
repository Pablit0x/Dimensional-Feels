package com.ps.happydays.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ps.happydays.presentation.screens.auth.AuthenticationScreen

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
        AuthenticationScreen(isLoading = true, onSignInButtonClicked = {})
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route) {

    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}