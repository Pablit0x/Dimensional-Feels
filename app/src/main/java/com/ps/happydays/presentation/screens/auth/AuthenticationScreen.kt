package com.ps.happydays.presentation.screens.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ps.happydays.util.Constants.CLIENT_ID
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
    onSignInButtonClicked: () -> Unit,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit
) {
    Scaffold(content = { padding ->
        ContentWithMessageBar(messageBarState = messageBarState) {
            AuthenticationContent(
                isLoading = isLoading,
                onSignInButtonClicked = { onSignInButtonClicked() },
                modifier = Modifier.padding(padding)
            )
        }
    })

    OneTapSignInWithGoogle(state = oneTapSignInState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            onTokenIdReceived(tokenId)
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        })
}
