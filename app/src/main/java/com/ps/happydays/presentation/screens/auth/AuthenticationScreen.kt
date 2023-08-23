package com.ps.happydays.presentation.screens.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    isLoading: Boolean, onSignInButtonClicked: () -> Unit
) {
    Scaffold(content = { padding ->
        AuthenticationContent(
            isLoading = isLoading,
            onSignInButtonClicked = onSignInButtonClicked,
            modifier = Modifier.padding(padding)
        )
    })
}

@Preview
@Composable
fun AuthenticationScreenPreview() {
    AuthenticationScreen(isLoading = true) {}
}