package com.ps.dimensional_feels.presentation.screens.draw

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DrawScreen(
    onBackPressed: () -> Unit, onSavedPressed: (Bitmap) -> Unit
) {
    Scaffold(topBar = {
        DrawTopBar(onBackPressed = onBackPressed)
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            DrawingContent(onSavedPressed)
        }
    }
}