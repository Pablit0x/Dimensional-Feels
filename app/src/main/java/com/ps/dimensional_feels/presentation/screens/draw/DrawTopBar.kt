package com.ps.dimensional_feels.presentation.screens.draw

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ps.dimensional_feels.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawTopBar(
    onBackPressed: () -> Unit
) {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.canvas)) },
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_back)
                )
            }
        })
}