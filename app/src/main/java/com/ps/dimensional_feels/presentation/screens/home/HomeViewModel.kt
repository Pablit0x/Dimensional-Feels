package com.ps.dimensional_feels.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.ps.dimensional_feels.data.repository.Diaries
import com.ps.dimensional_feels.data.repository.MongoRepository
import com.ps.dimensional_feels.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val user: User?,
    private val mongoRepository: MongoRepository,
    private val firebaseAuth: FirebaseAuth,
    val firebaseStorage: FirebaseStorage
) : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    private lateinit var allDiariesJob: Job
    private lateinit var dateFilteredDiariesJob: Job
    private lateinit var textFilteredDiariesJob: Job


    var dateIsSelected by mutableStateOf(false)
        private set

    init {
        getDiaries()
    }

    fun getDiaries(zonedDateTime: ZonedDateTime? = null, searchText: String? = null) {
        dateIsSelected = zonedDateTime != null
        diaries.value = RequestState.Loading
        if (dateIsSelected && zonedDateTime != null) {
            observeDateFilteredDiaries(zonedDateTime = zonedDateTime)
        } else if (searchText != null) {
            observeTextFilteredDiaries(searchText = searchText)
        } else {
            observeAllDiaries()
        }
    }

    private fun observeDateFilteredDiaries(zonedDateTime: ZonedDateTime) {
        dateFilteredDiariesJob = viewModelScope.launch {
            if (::allDiariesJob.isInitialized) {
                allDiariesJob.cancelAndJoin()
            }
            if (::textFilteredDiariesJob.isInitialized) {
                textFilteredDiariesJob.cancelAndJoin()
            }
            mongoRepository.getDateFilteredDiaries(zonedDateTime).collect {
                diaries.value = it
            }
        }
    }

    private fun observeTextFilteredDiaries(searchText: String) {
        textFilteredDiariesJob = viewModelScope.launch {
            if (::allDiariesJob.isInitialized) {
                allDiariesJob.cancelAndJoin()
            }
            if (::dateFilteredDiariesJob.isInitialized) {
                dateFilteredDiariesJob.cancelAndJoin()
            }
            mongoRepository.getTextFilteredDiaries(searchText)
                .collect {
                    diaries.value = it
                }
        }
    }

    private fun observeAllDiaries() {
        allDiariesJob = viewModelScope.launch {
            if (::dateFilteredDiariesJob.isInitialized) {
                dateFilteredDiariesJob.cancelAndJoin()
            }
            if (::textFilteredDiariesJob.isInitialized) {
                textFilteredDiariesJob.cancelAndJoin()
            }
            mongoRepository.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }

    fun logOut(navigateToAuth: () -> Unit) {
        firebaseAuth.signOut()
        viewModelScope.launch {
            user?.let {
                it.logOut()
                withContext(Dispatchers.Main) {
                    navigateToAuth()
                }
            }
        }
    }

}