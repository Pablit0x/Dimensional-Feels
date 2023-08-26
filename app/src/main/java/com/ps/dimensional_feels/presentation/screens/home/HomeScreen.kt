package com.ps.dimensional_feels.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.data.repository.Diaries
import com.ps.dimensional_feels.util.RequestState

@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onClick: () -> Unit,
    onMenuClicked: () -> Unit,
    onNavigateToWrite: () -> Unit
) {

    NavigationDrawer(drawerState = drawerState, onSignOutClicked = onSignOutClicked) {
        Scaffold(topBar = {
            HomeTopBar(onMenuClicked = onMenuClicked)
        }, floatingActionButton = {
            FloatingActionButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.add_new_diary_icon)
                )
            }
        }, content = { padding ->
            when (diaries) {
                is RequestState.Success -> {
                    HomeContent(paddingValues = padding, diariesNotes = diaries.data, onClick = {})
                }

                is RequestState.Error -> {
                    EmptyDiaryPage(
                        modifier = Modifier.padding(padding),
                        title = stringResource(id = R.string.error_diary_title),
                        description = "${diaries.error.message}"
                    )
                }

                is RequestState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                else -> {}
            }
        })
    }
}