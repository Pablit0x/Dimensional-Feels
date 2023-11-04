package com.ps.dimensional_feels.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.navigation.Screen

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onDeleteAllClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onSettingsClicked: () -> Unit = {},
    onHomeClicked: () -> Unit = {},
    currentScreen: Screen,
    content: @Composable () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.morty))
    var isPlaying by remember { mutableStateOf(true) }
    val progress by animateLottieCompositionAsState(
        composition = composition, isPlaying = isPlaying
    )

    val homeIcon = if (currentScreen == Screen.Home) Icons.Filled.Home else Icons.Outlined.Home
    val settingsIcon =
        if (currentScreen == Screen.Settings) Icons.Filled.Settings else Icons.Outlined.Settings

    LaunchedEffect(key1 = progress) {
        if (progress == 0f) {
            isPlaying = true
        }
        if (progress == 1f) {
            isPlaying = false
        }
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {

                LottieAnimation(composition = composition,
                    modifier = Modifier.fillMaxSize(),
                    progress = {
                        if (progress == 1f) {
                            isPlaying = true
                        }
                        progress
                    })
            }

            NavigationDrawerItem(modifier = Modifier.padding(horizontal = 12.dp), label = {
                Row {
                    Icon(
                        imageVector = homeIcon, contentDescription = stringResource(
                            id = R.string.home_route
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.home),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }, selected = currentScreen == Screen.Home, onClick = onHomeClicked)

            NavigationDrawerItem(modifier = Modifier.padding(horizontal = 12.dp), label = {
                Row {
                    Icon(
                        imageVector = settingsIcon, contentDescription = stringResource(
                            id = R.string.settings_route
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.settings),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }, selected = currentScreen == Screen.Settings, onClick = onSettingsClicked)

            NavigationDrawerItem(
                modifier = Modifier.padding(horizontal = 12.dp), label = {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Logout, contentDescription = stringResource(
                                id = R.string.google_logo
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(id = R.string.google_sign_out),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }, selected = false, onClick = onSignOutClicked
            )
        }
    }, content = content)
}