package com.ps.dimensional_feels.presentation.screens.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.dimensional_feels.data.repository.MongoDb
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.Mood
import com.ps.dimensional_feels.model.getMoodByName
import com.ps.dimensional_feels.model.toRickAndMortyCharacter
import com.ps.dimensional_feels.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY
import com.ps.dimensional_feels.util.RequestState
import com.ps.dimensional_feels.util.exceptions.DiaryAlreadyDeletedException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class WriteViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var uiState by mutableStateOf(WriteUiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    private fun getDiaryIdArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARGUMENT_KEY
            )
        )
    }

    private fun fetchSelectedDiary() {
        uiState.selectedDiaryId?.let { selectedId ->
            viewModelScope.launch {
                MongoDb.getSelectedDiary(diaryId = ObjectId(hexString = selectedId)).catch {
                    emit(RequestState.Error(DiaryAlreadyDeletedException()))
                }.collect { diary ->
                    if (diary is RequestState.Success) {
                        setSelectedDiary(diary = diary.data)
                        setMood(
                            mood = getMoodByName(
                                name = diary.data.mood,
                                character = diary.data.character.toRickAndMortyCharacter()
                            )
                        )
                        setTitle(title = diary.data.title)
                        setDescription(description = diary.data.description)

                    }
                }
            }
        }
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun setMood(mood: Mood) {
        uiState = uiState.copy(
            mood = mood
        )
    }

    fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(
            selectedDiary = diary
        )
    }
}