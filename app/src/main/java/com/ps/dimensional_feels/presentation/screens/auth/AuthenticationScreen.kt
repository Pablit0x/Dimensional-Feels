package com.ps.dimensional_feels.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.ps.dimensional_feels.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
    isLoading: Boolean,
    authenticated: Boolean,
    onSignInButtonClicked: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
    navigateHome: () -> Unit
) {

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            navigateHome()
        }
    }

    Scaffold(
        content = { padding ->
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    isLoading = isLoading,
                    onSignInButtonClicked = { onSignInButtonClicked() },
                    modifier = Modifier.padding(padding)
                )
            }
        }, modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding()
    )

    OneTapSignInWithGoogle(state = oneTapSignInState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            onTokenIdReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        })
}
