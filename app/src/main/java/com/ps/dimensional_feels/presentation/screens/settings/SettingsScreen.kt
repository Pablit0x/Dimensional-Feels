package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.navigation.Screen
import com.ps.dimensional_feels.presentation.components.NavigationDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    drawerState: DrawerState,
    onClearDiaryClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onHomeClicked: () -> Unit
) {

    val scope = rememberCoroutineScope()

    NavigationDrawer(
        drawerState = drawerState,
        onDeleteAllClicked = onClearDiaryClicked,
        onSignOutClicked = onSignOutClicked,
        onHomeClicked = onHomeClicked,
        currentScreen = Screen.Settings
    ) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(id = R.string.hamburger_menu_icon)
                        )
                    }
                })
        }) { padding ->
            Column(
                modifier = Modifier.padding(padding), verticalArrangement = Arrangement.Center
            ) {
                SettingsContent(
                    onSignOutClicked = onSignOutClicked,
                    onClearDiaryClicked = onClearDiaryClicked,
                    onDeleteAccountClicked = onDeleteAccountClicked,
                    modifier = Modifier.padding(16.dp).fillMaxSize()
                )
            }
        }
    }
}