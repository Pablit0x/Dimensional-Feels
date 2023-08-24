package com.ps.happydays.presentation.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps.happydays.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {
    var loadingState = mutableStateOf(false)
        private set

    var authenticated = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        credentials = Credentials.jwt(jwtToken = tokenId)
                    ).loggedIn
                }
                withContext(Dispatchers.Main) {
                    if (result) {
                        onSuccess()
                        delay(600)
                        authenticated.value = true
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            } finally {
                loadingState.value = false
            }
        }
    }
}