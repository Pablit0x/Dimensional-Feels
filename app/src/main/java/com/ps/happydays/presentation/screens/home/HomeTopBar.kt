package com.ps.happydays.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ps.happydays.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onMenuClicked: () -> Unit) {
    TopAppBar(navigationIcon = {
        IconButton(onClick = onMenuClicked) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.hamburger_menu_icon)
            )
        }
    }, title = {
        Text(text = stringResource(id = R.string.home_top_bar_title))
    }, actions = {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(id = R.string.select_date_icon)
            )
        }
    })
}