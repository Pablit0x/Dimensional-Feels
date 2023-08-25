package com.ps.dimensional_feels.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.presentation.components.CustomAlertDialog
import com.ps.dimensional_feels.presentation.screens.auth.AuthenticationScreen
import com.ps.dimensional_feels.presentation.screens.auth.AuthenticationViewModel
import com.ps.dimensional_feels.presentation.screens.home.HomeScreen
import com.ps.dimensional_feels.util.Constants.APP_ID
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestinationRoute: String, navController: NavHostController
) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        authenticationRoute(navigateHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        })
        homeRoute(onNavigateToWrite = {
            navController.navigate(Screen.Write.route)
        }, navigateToAuth = {
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)
        })
        writeRoute()
    }

}


fun NavGraphBuilder.authenticationRoute(navigateHome: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        AuthenticationScreen(
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            isLoading = loadingState,
            authenticated = authenticated,
            onSignInButtonClicked = {
                oneTapSignInState.open()
                viewModel.setLoading(loading = true)
            },
            onTokenIdReceived = { tokenId ->
                viewModel.signInWithMongoAtlas(tokenId = tokenId, onSuccess = {
                    messageBarState.addSuccess(message = "Success")
                }, onError = { errorMsg ->
                    messageBarState.addError(exception = Exception(errorMsg))
                })
            },
            onDialogDismissed = { errorMsg ->
                messageBarState.addError(Exception(errorMsg))
                viewModel.setLoading(loading = false)
            },
            navigateHome = navigateHome
        )
    }
}

fun NavGraphBuilder.homeRoute(onNavigateToWrite: () -> Unit, navigateToAuth: () -> Unit) {
    composable(route = Screen.Home.route) {
        var isSignOutDialogOpen by remember { mutableStateOf(false) }
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        HomeScreen(drawerState = drawerState,
            onSignOutClicked = { isSignOutDialogOpen = true },
            onMenuClicked = { scope.launch { drawerState.open() } },
            onNavigateToWrite = { onNavigateToWrite() })

        CustomAlertDialog(title = stringResource(id = R.string.google_sign_out),
            message = stringResource(
                id = R.string.sign_out_message
            ),
            isOpen = isSignOutDialogOpen,
            onCloseDialog = { isSignOutDialogOpen = false },
            onConfirmClicked = {
                scope.launch(Dispatchers.IO) {
                    App.create(APP_ID).currentUser?.let { user ->
                        user.logOut()
                        withContext(Dispatchers.Main) {
                            navigateToAuth()
                        }
                    }
                }
            })
    }
}

fun NavGraphBuilder.writeRoute() {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {

    }
}