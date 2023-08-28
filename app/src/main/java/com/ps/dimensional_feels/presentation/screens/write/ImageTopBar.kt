package com.ps.dimensional_feels.presentation.screens.write

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
fun ImageTopBar(title: String, onBackClicked: () -> Unit, onDeleteClicked: () -> Unit) {
    CenterAlignedTopAppBar(title = { Text(text = title) }, navigationIcon = {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.navigate_back)
            )
        }
    }, actions = {
        IconButton(onClick = onDeleteClicked) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_image)
            )
        }
    })
}