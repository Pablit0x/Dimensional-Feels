package com.ps.dimensional_feels.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ps.dimensional_feels.model.Diary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(selectedDiary: Diary?, onBackPressed: () -> Unit, onDeleteConfirmed: () -> Unit) {
    Scaffold(topBar = {
        WriteTopBar(
            selectedDiary = selectedDiary,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = onDeleteConfirmed
        )
    }, content = {})
}