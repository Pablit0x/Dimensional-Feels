package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.navigation.Screen
import com.ps.dimensional_feels.presentation.components.NavigationDrawer
import com.ps.dimensional_feels.presentation.screens.auth.AuthenticationContent
import com.ps.dimensional_feels.presentation.theme.PortalPurple
import com.ps.dimensional_feels.util.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    drawerState: DrawerState,
    firebaseAuth: FirebaseAuth,
    onSwitchToGoogleClicked: () -> Unit,
    onSuccessfulFirebaseSignIn: (String) -> Unit,
    onFailedFirebaseSignIn: (Exception) -> Unit,
    onDialogDismissed: (String) -> Unit,
    onClearDiaryClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    onHomeClicked: () -> Unit,
    onAlarmCanceled: () -> Unit,
    onAlarmScheduled: (Calendar) -> Unit,
    onUpdateReminderStatusPrefs: (Boolean) -> Unit,
    onUpdateReminderTimePrefs: (LocalTime) -> Unit,
    isDailyReminderEnabled: Boolean,
    dailyReminderTime: LocalTime,
    isAnonymous: Boolean,
    isGoogleLoading: Boolean
) {

    val scope = rememberCoroutineScope()
    val oneTapSignIn = rememberOneTapSignInState()
    val messageBarState = rememberMessageBarState()

    NavigationDrawer(
        drawerState = drawerState,
        onSignOutClicked = onSignOutClicked,
        onHomeClicked = onHomeClicked,
        currentScreen = Screen.Settings
    ) {

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.settings)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(id = R.string.hamburger_menu_icon)
                            )
                        }
                    })
            },
            content = { padding ->
                ContentWithMessageBar(
                    messageBarState = messageBarState,
                    successContainerColor = PortalPurple
                ) {
                    Column(
                        modifier = Modifier.padding(padding),
                        verticalArrangement = Arrangement.Center
                    ) {
                        SettingsContent(
                            onSwitchToGoogleClicked = onSwitchToGoogleClicked,
                            onSignOutClicked = onSignOutClicked,
                            onClearDiaryClicked = onClearDiaryClicked,
                            onDeleteAccountClicked = onDeleteAccountClicked,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            onAlarmCanceled = onAlarmCanceled,
                            onAlarmScheduled = onAlarmScheduled,
                            onUpdateReminderStatusPrefs = onUpdateReminderStatusPrefs,
                            onUpdateReminderTimePrefs = onUpdateReminderTimePrefs,
                            isDailyReminderEnabled = isDailyReminderEnabled,
                            dailyReminderTime = dailyReminderTime,
                            isAnonymous = isAnonymous,
                            isGoogleLoading = isGoogleLoading
                        )
                    }
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .statusBarsPadding()
                .navigationBarsPadding()
        )

        OneTapSignInWithGoogle(state = oneTapSignIn,
            clientId = Constants.CLIENT_ID,
            onTokenIdReceived = { tokenId ->
                val credential = GoogleAuthProvider.getCredential(tokenId, null)
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        onSuccessfulFirebaseSignIn(tokenId)
                    } else {
                        result.exception?.let(onFailedFirebaseSignIn)
                    }
                }
            },
            onDialogDismissed = { message ->
                onDialogDismissed(message)
            })
    }
}