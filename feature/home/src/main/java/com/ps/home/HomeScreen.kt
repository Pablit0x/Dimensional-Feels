package com.ps.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.ps.mongo.repository.Diaries
import com.ps.ui.components.EmptyPage
import com.ps.util.R
import com.ps.util.RequestState
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onDeleteAllClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    onNavigateToWrite: () -> Unit,
    onNavigateToWriteWithArgs: (String) -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    onDateReset: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    NavigationDrawer(
        drawerState = drawerState,
        onDeleteAllClicked = onDeleteAllClicked,
        onSignOutClicked = onSignOutClicked
    ) {
        Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                onMenuClicked = onMenuClicked,
                dateIsSelected = dateIsSelected,
                onDateSelected = onDateSelected,
                onDateReset = onDateReset
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
                        onClick = onNavigateToWriteWithArgs
                    )
                }

                is RequestState.Error -> {
                    EmptyPage(
                        modifier = Modifier.padding(padding),
                        title = stringResource(id = R.string.error_diary_title),
                        description = "${diaries.error.message}"
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