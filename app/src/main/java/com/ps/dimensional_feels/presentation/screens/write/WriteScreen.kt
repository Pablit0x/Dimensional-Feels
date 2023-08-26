package com.ps.dimensional_feels.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.ps.dimensional_feels.model.Diary

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedDiary: Diary?,
    pagerState: PagerState,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    Scaffold(topBar = {
        WriteTopBar(
            selectedDiary = selectedDiary,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = onDeleteConfirmed
        )
    }, content = { padding ->
        WriteContent(paddingValues = padding,
            pagerState = pagerState,
            title = "",
            description = "",
            onTitleChanged = {},
            onDescriptionChanged = {})
    })
}