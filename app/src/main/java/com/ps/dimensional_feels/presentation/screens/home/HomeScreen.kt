package com.ps.dimensional_feels.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ps.dimensional_feels.R

@Composable
fun HomeScreen(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    onNavigateToWrite: () -> Unit
) {

    NavigationDrawer(drawerState = drawerState, onSignOutClicked = onSignOutClicked) {
        Scaffold(content = { padding ->
            Column(modifier = Modifier.padding(padding)) {

            }
        }, topBar = {
            HomeTopBar(onMenuClicked = onMenuClicked)
        }, floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToWrite) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.add_new_diary_icon)
                )
            }
        })
    }
}