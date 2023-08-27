package com.ps.dimensional_feels.navigation

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.getMoodByPosition
import com.ps.dimensional_feels.model.toRickAndMortyCharacter
import com.ps.dimensional_feels.presentation.components.CustomAlertDialog
import com.ps.dimensional_feels.presentation.screens.auth.AuthenticationScreen
import com.ps.dimensional_feels.presentation.screens.auth.AuthenticationViewModel
import com.ps.dimensional_feels.presentation.screens.home.HomeScreen
import com.ps.dimensional_feels.presentation.screens.home.HomeViewModel
import com.ps.dimensional_feels.presentation.screens.write.WriteScreen
import com.ps.dimensional_feels.presentation.screens.write.WriteViewModel
import com.ps.dimensional_feels.util.Constants.APP_ID
import com.ps.dimensional_feels.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetupNavGraph(
    startDestinationRoute: String, navController: NavHostController, onDataLoaded: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        authenticationRoute(navigateHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        }, onDataLoaded = onDataLoaded)
        homeRoute(onNavigateToWriteWithArgs = {
            navController.navigate(Screen.Write.passDiaryId(it))
        }, onNavigateToWrite = {
            navController.navigate(Screen.Write.route)
        }, navigateToAuth = {
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)
        }, onDataLoaded = onDataLoaded
        )
        writeRoute(onBackPressed = {
            navController.popBackStack()
        })
    }

}


fun NavGraphBuilder.authenticationRoute(navigateHome: () -> Unit, onDataLoaded: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

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

fun NavGraphBuilder.homeRoute(
    onNavigateToWriteWithArgs: (String) -> Unit,
    onNavigateToWrite: () -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val viewModel: HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        var isSignOutDialogOpen by remember { mutableStateOf(false) }
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(diaries = diaries,
            drawerState = drawerState,
            onSignOutClicked = { isSignOutDialogOpen = true },
            onMenuClicked = { scope.launch { drawerState.open() } },
            onNavigateToWriteWithArgs = onNavigateToWriteWithArgs,
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

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(onBackPressed: () -> Unit) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) {
        val pagerState = rememberPagerState()
        val viewModel: WriteViewModel = viewModel()
        val uiState = viewModel.uiState
        val pageNumber by remember {
            derivedStateOf {
                pagerState.currentPage
            }
        }

        LaunchedEffect(uiState) {
            Log.d("lolipop", "State $uiState")
        }

        WriteScreen(uiState = uiState,
            pagerState = pagerState,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = {},
            moodName = {
                getMoodByPosition(
                    position = pageNumber,
                    character = uiState.characters.name.toRickAndMortyCharacter()
                ).name
            },
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onSavedClicked = {
                viewModel.insertDiary(diary = it.apply {
                    this.mood = getMoodByPosition(
                        position = pageNumber,
                        character = uiState.characters.name.toRickAndMortyCharacter()
                    ).name
                }, onSuccess = { onBackPressed() }, {})
            })
    }
}