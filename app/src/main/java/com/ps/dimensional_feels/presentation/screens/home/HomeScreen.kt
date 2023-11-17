package com.ps.dimensional_feels.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.google.firebase.storage.FirebaseStorage
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.data.repository.Diaries
import com.ps.dimensional_feels.navigation.Screen
import com.ps.dimensional_feels.presentation.components.EmptyPage
import com.ps.dimensional_feels.presentation.components.NavigationDrawer
import com.ps.dimensional_feels.util.RequestState
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    firebaseStorage: FirebaseStorage,
    diaries: Diaries,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    onNavigateToWrite: () -> Unit,
    onNavigateToWriteWithArgs: (String) -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit,
    onSearch: (String) -> Unit,
    onSearchReset: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var isSearchOpen by rememberSaveable { mutableStateOf(false) }

    NavigationDrawer(
        drawerState = drawerState,
        onSignOutClicked = onSignOutClicked,
        onSettingsClicked = onSettingsClicked,
        currentScreen = Screen.Home
    ) {
        Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                onMenuClicked = onMenuClicked,
                dateIsSelected = dateIsSelected,
                onDateSelected = onDateSelected,
                onDateReset = onDateReset,
                onSearchClicked = {
                    isSearchOpen = !isSearchOpen
                },
                searchActive = isSearchOpen
            )
        }, floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToWrite) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(id = R.string.add_new_diary_icon)
                )
            }
        }, content = { padding ->
            when (diaries) {
                is RequestState.Success -> {
                    HomeContent(
                        paddingValues = padding,
                        diariesNotes = diaries.data,
                        onClick = onNavigateToWriteWithArgs,
                        navigateToWrite = onNavigateToWrite,
                        isSearchOpen = isSearchOpen,
                        isDateSelected = dateIsSelected,
                        onSearch = { onSearch(it) },
                        onSearchReset = onSearchReset,
                        firebaseStorage = firebaseStorage
                    )
                }

                is RequestState.Error -> {
                    EmptyPage(
                        modifier = Modifier.padding(padding),
                        title = stringResource(id = R.string.error_diary_title)
                    )
                }

                is RequestState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else -> {}
            }
        })
    }
}