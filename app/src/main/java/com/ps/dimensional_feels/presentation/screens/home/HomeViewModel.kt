package com.ps.dimensional_feels.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.ps.dimensional_feels.connectivity.ConnectivityObserver
import com.ps.dimensional_feels.connectivity.NetworkConnectivityObserver
import com.ps.dimensional_feels.data.database.ImageToDeleteDao
import com.ps.dimensional_feels.data.database.entity.ImageToDelete
import com.ps.dimensional_feels.data.repository.Diaries
import com.ps.dimensional_feels.data.repository.MongoDb
import com.ps.dimensional_feels.util.RequestState
import com.ps.dimensional_feels.util.exceptions.NoInternetConnectionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageToDeleteDao: ImageToDeleteDao,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    init {
        observeAllDiaries()
        observeConnectivityObserver()
    }

    private fun observeConnectivityObserver() {
        viewModelScope.launch {
            connectivityObserver.observe().collectLatest {
                network = it
            }
        }
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoDb.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }

    fun deleteAllDiaries(
        onSuccess: () -> Unit, onError: (Throwable) -> Unit
    ) {
        if (network == ConnectivityObserver.Status.Available) {
            val firebaseUserId = FirebaseAuth.getInstance().currentUser?.uid
            val imagesDirectory = "images/${firebaseUserId}"
            val storage = FirebaseStorage.getInstance().reference
            storage.child(imagesDirectory).listAll().addOnSuccessListener {
                it.items.forEach { ref ->
                    val imagePath = "images/${firebaseUserId}/${ref.name}"
                    storage.child(imagePath).delete().addOnFailureListener {
                        viewModelScope.launch(Dispatchers.IO) {
                            imageToDeleteDao.addImageToDelete(ImageToDelete(remoteImagePath = imagePath))
                        }
                    }
                }
                viewModelScope.launch(Dispatchers.IO) {
                    val result = MongoDb.deleteAllDiaries()
                    if (result is RequestState.Success) {
                        withContext(Dispatchers.Main) {
                            onSuccess()
                        }
                    } else if (result is RequestState.Error) {
                        withContext(Dispatchers.Main) {
                            onError(result.error)
                        }
                    }
                }
            }.addOnFailureListener { e ->
                onError(e)
            }
        } else {
            onError(NoInternetConnectionException())
        }
    }
}