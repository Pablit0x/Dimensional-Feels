package com.ps.write

import com.ps.util.model.Diary
import com.ps.util.model.Mood
import com.ps.util.model.RickAndMortyCharacters
import io.realm.kotlin.types.RealmInstant

data class WriteUiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Happy(),
    val characters: RickAndMortyCharacters = RickAndMortyCharacters.Rick,
    val updatedDateTime: RealmInstant? = null
)
