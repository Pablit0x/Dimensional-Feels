package com.ps.dimensional_feels.presentation.screens.write

import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.Mood
import com.ps.dimensional_feels.model.RickAndMortyCharacters

data class WriteUiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Happy(),
    val characters: RickAndMortyCharacters = RickAndMortyCharacters.Rick
)
