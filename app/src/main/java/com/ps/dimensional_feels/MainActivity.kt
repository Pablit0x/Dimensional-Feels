package com.ps.dimensional_feels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.ps.dimensional_feels.navigation.Screen
import com.ps.dimensional_feels.navigation.SetupNavGraph
import com.ps.dimensional_feels.presentation.theme.DimensionalFeelsTheme
import com.ps.dimensional_feels.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DimensionalFeelsTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestinationRoute = getStartDestination(), navController = navController
                )
            }
        }
    }
}

private fun getStartDestination(): String {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route else Screen.Authentication.route
}