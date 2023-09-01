package com.ps.dimensional_feels.presentation.screens.write

import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.Mood
import com.ps.dimensional_feels.model.RickAndMortyCharacters
import io.realm.kotlin.types.RealmInstant

data class WriteUiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val characters: RickAndMortyCharacters = RickAndMortyCharacters.Rick,
    val mood: Mood = Mood.Happy(character = characters),
    val updatedDateTime: RealmInstant? = null
)
