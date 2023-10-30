package com.ps.dimensional_feels.presentation.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val app: App,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {
    var googleLoadingState = mutableStateOf(false)
        private set

    var guestLoadingState = mutableStateOf(false)
        private set

    var authenticated = mutableStateOf(false)
        private set

    fun setGoogleLoading(isLoading: Boolean) {
        googleLoadingState.value = isLoading
    }

    fun setGuestLoading(isLoading : Boolean) {
        guestLoadingState.value = isLoading
    }

    fun setAuthentication(isAuthenticated: Boolean){
        authenticated.value = isAuthenticated
    }

    fun signInWithMongoAtlas(
        tokenId: String?, onSuccess: () -> Unit, onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    if(tokenId != null){
                        app.login(
                            credentials = Credentials.google(
                                token = tokenId, type = GoogleAuthType.ID_TOKEN
                            )
                        ).loggedIn
                    } else {
                        app.login(credentials = Credentials.anonymous()).loggedIn
                    }
                }
                withContext(Dispatchers.Main) {
                    if (result) {
                        onSuccess()
                        delay(600)
                        setAuthentication(true)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            } finally {
                setGuestLoading(false)
                setGoogleLoading(false)
            }
        }
    }

    fun signInAnonymously(onSuccess: () -> Unit, onError: (Exception) -> Unit){
        firebaseAuth.signInAnonymously().addOnCompleteListener { result ->
            if(result.isSuccessful){
                onSuccess()
            } else {
                onError(result.exception ?: Exception("Anonymous sign-in failed. Please try again."))
            }
        }
    }
}