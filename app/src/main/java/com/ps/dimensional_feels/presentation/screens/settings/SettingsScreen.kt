package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.presentation.screens.home.NavigationDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(drawerState: DrawerState, onDeleteAllClicked : () -> Unit, onSignOutClicked: () -> Unit) {

    NavigationDrawer(
        drawerState = drawerState,
        onDeleteAllClicked = onDeleteAllClicked,
        onSignOutClicked = onSignOutClicked,
        onSettingsClicked = {}
    ){
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.settings)) })
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SettingsContent()
            }
        }
    }
}