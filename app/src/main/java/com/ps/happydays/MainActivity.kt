package com.ps.happydays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ps.happydays.navigation.Screen
import com.ps.happydays.navigation.SetupNavGraph
import com.ps.happydays.ui.theme.HappyDaysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            HappyDaysTheme {
                val navController = rememberNavController()

                SetupNavGraph(
                    startDestinationRoute = Screen.Authentication.route,
                    navController = navController
                )
            }
        }
    }
}