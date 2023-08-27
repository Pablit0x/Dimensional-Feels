package com.ps.dimensional_feels.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.getMoodByName
import com.ps.dimensional_feels.model.getPositionByMood
import com.ps.dimensional_feels.model.toRickAndMortyCharacter

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: WriteUiState,
    pagerState: PagerState,
    onBackPressed: () -> Unit,
    moodName: () -> String,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSavedClicked: (Diary) -> Unit
    ) {
    LaunchedEffect(key1 = uiState.mood) {
        val mood = getMoodByName(
            name = uiState.mood.name, character = uiState.characters.name.toRickAndMortyCharacter()
        )
        pagerState.scrollToPage(getPositionByMood(mood))
    }

    Scaffold(topBar = {
        WriteTopBar(
            selectedDiary = uiState.selectedDiary,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = onDeleteConfirmed,
            moodName = moodName
        )
    }, content = { padding ->
        WriteContent(
            uiState = uiState,
            paddingValues = padding,
            pagerState = pagerState,
            onTitleChanged = onTitleChanged,
            onDescriptionChanged = onDescriptionChanged,
            onSavedClicked = onSavedClicked,
        )
    })
}