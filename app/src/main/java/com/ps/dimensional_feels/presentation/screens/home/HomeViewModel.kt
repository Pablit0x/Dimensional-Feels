package com.ps.dimensional_feels.presentation.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.dimensional_feels.data.repository.Diaries
import com.ps.dimensional_feels.data.repository.MongoDb
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.Mood
import com.ps.dimensional_feels.model.RickAndMortyCharacters
import com.ps.dimensional_feels.util.RequestState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoDb.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }


    fun insert(userId: String){
        Log.d("lolipop", "insert attempt")
        viewModelScope.launch {
            MongoDb.insertDiary(
                Diary().apply {
                title = "Test"
                owner_id = userId
                description = "Test"
                character = RickAndMortyCharacters.Rick.name
                mood = Mood.Happy.name
            })
        }
    }
}