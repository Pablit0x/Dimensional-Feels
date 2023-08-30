package com.ps.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.ps.mongo.database.ImageToDeleteDao
import com.ps.mongo.database.entity.ImageToDelete
import com.ps.mongo.repository.Diaries
import com.ps.mongo.repository.MongoDb
import com.ps.util.RequestState
import com.ps.util.connectivity.ConnectivityObserver
import com.ps.util.connectivity.NetworkConnectivityObserver
import com.ps.util.exceptions.NoInternetConnectionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageToDeleteDao: ImageToDeleteDao,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)
    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    private lateinit var allDiariesJob: Job
    private lateinit var filteredDiariesJob: Job

    var dateIsSelected by mutableStateOf(false)
        private set

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

    fun getDiaries(zonedDateTime: ZonedDateTime? = null) {
        dateIsSelected = zonedDateTime != null
        diaries.value = RequestState.Loading
        if (dateIsSelected && zonedDateTime != null) {
            observeFilteredDiaries(zonedDateTime = zonedDateTime)
        } else {
            observeAllDiaries()
        }
    }

    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime) {

//        filteredDiariesJob = viewModelScope.launch {
//            if (::allDiariesJob.isInitialized) {
//                allDiariesJob.cancelAndJoin()
//            }
//            MongoDb.getFilteredDiaries(zonedDateTime).collect {
//                diaries.value = it
//            }
//        }
    }

    private fun observeAllDiaries() {
        MongoDb.logErr()
//        allDiariesJob = viewModelScope.launch {
////            if (::filteredDiariesJob.isInitialized) {
////                filteredDiariesJob.cancelAndJoin()
////            }
////            MongoDb.configureTheRealm()
//            MongoDb.getAllDiaries().collect { result ->
//                diaries.value = result
//            }
//        }
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