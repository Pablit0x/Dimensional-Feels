package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.ps.dimensional_feels.alarm.AlarmScheduler
import com.ps.dimensional_feels.connectivity.ConnectivityObserver
import com.ps.dimensional_feels.connectivity.NetworkConnectivityObserver
import com.ps.dimensional_feels.data.database.ImageToDeleteDao
import com.ps.dimensional_feels.data.database.entity.ImageToDelete
import com.ps.dimensional_feels.data.repository.MongoRepository
import com.ps.dimensional_feels.util.Constants
import com.ps.dimensional_feels.util.PreferencesManager
import com.ps.dimensional_feels.util.RequestState
import com.ps.dimensional_feels.util.exceptions.NoInternetConnectionException
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import io.realm.kotlin.mongodb.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val app: App,
    private val mongoRepository: MongoRepository,
    private val imageToDeleteDao: ImageToDeleteDao,
    private val connectivityObserver: NetworkConnectivityObserver,
    private val user: User?,
    private val preferencesManager: PreferencesManager,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    var isGoogleLoading = mutableStateOf(false)
        private set

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    var isDailyReminderEnabled by mutableStateOf(
        preferencesManager.getBoolean(
            Constants.IS_DAILY_REMINDER_ENABLED_KEY, false
        )
    )
        private set


    var isUserAnonymous by mutableStateOf(firebaseAuth.currentUser?.isAnonymous)
        private set

    var dailyReminderTime: LocalTime by mutableStateOf(
        LocalTime.of(
            preferencesManager.getInt(Constants.DAILY_REMINDER_HOUR_KEY, 20),
            preferencesManager.getInt(Constants.DAILY_REMINDER_MINUTE_KEY, 0)
        )
    )
        private set

    init {
        observeConnectivityObserver()
    }

    private fun observeConnectivityObserver() {
        viewModelScope.launch {
            connectivityObserver.observe().collectLatest {
                network = it
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

    fun deleteAccount(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAllDiaries(onSuccess = {
                firebaseAuth.currentUser?.delete()?.addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        updateReminderStatusPrefs(isReminderEnabled = false)
                        cancelAlarm()
                        onSuccess()
                    } else {
                        result.exception?.cause?.let { onError(it) }
                    }
                }
            }, onError = onError)
        }
    }

    fun deleteAllDiaries(
        onSuccess: () -> Unit, onError: (Throwable) -> Unit
    ) {
        if (network == ConnectivityObserver.Status.Available) {
            val firebaseUserId = firebaseAuth.currentUser?.uid
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
                    val result = mongoRepository.deleteAllDiaries()
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

    fun setGoogleLoading(isLoading: Boolean){
        isGoogleLoading.value = isLoading
    }

    fun scheduleAlarm(calendar: Calendar) {
        alarmScheduler.schedule(calendar)
    }

    fun cancelAlarm() {
        alarmScheduler.cancelAlarm()
    }

    fun updateReminderStatusPrefs(isReminderEnabled: Boolean) {
        preferencesManager.saveBoolean(Constants.IS_DAILY_REMINDER_ENABLED_KEY, isReminderEnabled)
        isDailyReminderEnabled = isReminderEnabled
    }

    fun updateReminderTimePrefs(alarmTime: LocalTime) {
        preferencesManager.saveInt(Constants.DAILY_REMINDER_HOUR_KEY, alarmTime.hour)
        preferencesManager.saveInt(Constants.DAILY_REMINDER_MINUTE_KEY, alarmTime.minute)
        dailyReminderTime = alarmTime
    }


    fun switchFromAnonymousToGoogleAccount(
        tokenId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit
    ) {
        val anonymousId = user!!.id
        logOut(navigateToAuth = {})
        viewModelScope.launch {
            try {
                val authResult = withContext(Dispatchers.IO) {
                    app.login(
                        credentials = Credentials.google(
                            token = tokenId, type = GoogleAuthType.ID_TOKEN
                        )
                    ).loggedIn
                }

                val transferResult = mongoRepository.transferAllDiariesToGoogleAccount(anonymousId)

                withContext(Dispatchers.Main) {
                    if (authResult && transferResult is RequestState.Success) {
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            } finally {
                setGoogleLoading(false)
            }
        }
    }
}