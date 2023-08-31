package com.ps.dimensional_feels.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.ps.dimensional_feels.util.RequestState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    startDestinationRoute: String, navController: NavHostController, onDataLoaded: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        authenticationRoute(navigateHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        }, onDataLoaded = onDataLoaded)
        homeRoute(onNavigateToCalendarWithArgs = {
//            navController.navigate(Screen.Calendar.passCurrentDate(123456L))
        }, onNavigateToWriteWithArgs = {
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
        calendarRoute(onCancelPressed = {}, onSelectDate = {})
    }

}


fun NavGraphBuilder.authenticationRoute(navigateHome: () -> Unit, onDataLoaded: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val context = LocalContext.current
        val viewModel = hiltViewModel<AuthenticationViewModel>()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated
        val firebaseAuth = viewModel.firebaseAuthentication
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

        AuthenticationScreen(
            firebaseAuth = firebaseAuth,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            isLoading = loadingState,
            authenticated = authenticated,
            onSignInButtonClicked = {
                oneTapSignInState.open()
                viewModel.setLoading(loading = true)
            },
            onSuccessfulFirebaseSignIn = { tokenId ->
                viewModel.signInWithMongoAtlas(tokenId = tokenId, onSuccess = {
                    messageBarState.addSuccess(message = context.getString(R.string.success))
                }, onError = { errorMsg ->
                    messageBarState.addError(exception = Exception(errorMsg))
                })
            },
            onFailedFirebaseSignIn = {
                messageBarState.addError(it)
                viewModel.setLoading(false)
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
    onNavigateToCalendarWithArgs: (Long) -> Unit,
    onNavigateToWriteWithArgs: (String) -> Unit,
    onNavigateToWrite: () -> Unit,
    navigateToAuth: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val context = LocalContext.current
        val viewModel = hiltViewModel<HomeViewModel>()
        val diaries by viewModel.diaries
        var isSignOutDialogOpen by remember { mutableStateOf(false) }
        var isDeleteAllDialogOpen by remember { mutableStateOf(false) }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
            onNavigateToCalendarWithArgs(1234L)
        }

        HomeScreen(diaries = diaries,
            drawerState = drawerState,
            onDeleteAllClicked = { isDeleteAllDialogOpen = true },
            onSignOutClicked = { isSignOutDialogOpen = true },
            onMenuClicked = { scope.launch { drawerState.open() } },
            onNavigateToWriteWithArgs = onNavigateToWriteWithArgs,
            onNavigateToWrite = { onNavigateToWrite() },
            dateIsSelected = viewModel.dateIsSelected,
            onDateSelected = {
                viewModel.getDiaries(zonedDateTime = it)
            },
            onDateReset = {
                viewModel.getDiaries()
            })

        CustomAlertDialog(title = stringResource(id = R.string.delete_all_diaries),
            message = stringResource(
                id = R.string.delete_all_diaries_message
            ),
            isOpen = isDeleteAllDialogOpen,
            onCloseDialog = { isDeleteAllDialogOpen = false },
            onConfirmClicked = {
                viewModel.deleteAllDiaries(onSuccess = {
                    Toast.makeText(
                        context, context.getString(R.string.all_diaries_deleted), Toast.LENGTH_SHORT
                    ).show()
                    scope.launch {
                        drawerState.close()
                    }
                }, onError = {
                    Toast.makeText(
                        context,
                        it.message ?: context.getString(R.string.unknown_error),
                        Toast.LENGTH_SHORT
                    ).show()
                })
            })


        CustomAlertDialog(title = stringResource(id = R.string.google_sign_out),
            message = stringResource(
                id = R.string.sign_out_message
            ),
            isOpen = isSignOutDialogOpen,
            onCloseDialog = { isSignOutDialogOpen = false },
            onConfirmClicked = {
                viewModel.logOut(navigateToAuth = { navigateToAuth() })
            })
    }
}

fun NavGraphBuilder.calendarRoute(onCancelPressed: () -> Unit, onSelectDate: (Long) -> Unit) {
    composable(
        route = Screen.Calendar.route,
        arguments = listOf(navArgument(name = NavigationArguments.CALENDAR_SCREEN_ARGUMENT_KEY) {
            type = NavType.LongType
            defaultValue = 0L
        })
    ) { entry ->
        val x = entry.arguments?.getLong(NavigationArguments.CALENDAR_SCREEN_ARGUMENT_KEY)

        Log.d("lolipop", "entry = $x")
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Calendar $x")
        }
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
        val context = LocalContext.current
        val pagerState = rememberPagerState()
        val viewModel = hiltViewModel<WriteViewModel>()
        val galleryState = viewModel.galleryState
        val uiState = viewModel.uiState
        val pageNumber by remember {
            derivedStateOf {
                pagerState.currentPage
            }
        }

        WriteScreen(uiState = uiState,
            galleryState = galleryState,
            pagerState = pagerState,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = {
                viewModel.deleteDiary(onSuccess = {
                    onBackPressed()
                }, onError = {})
            },
            moodName = {
                getMoodByPosition(
                    position = pageNumber,
                    character = uiState.characters.name.toRickAndMortyCharacter()
                ).name
            },
            onTitleChanged = { viewModel.setTitle(title = it) },
            onDescriptionChanged = { viewModel.setDescription(description = it) },
            onSavedClicked = {
                viewModel.upsertDiary(diary = it.apply {
                    this.mood = getMoodByPosition(
                        position = pageNumber,
                        character = uiState.characters.name.toRickAndMortyCharacter()
                    ).name
                }, onSuccess = { onBackPressed() }, {})
            },
            onDateTimeUpdated = {
                viewModel.updateDateTime(zonedDateTime = it)
            },
            onImageSelected = {
                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
                viewModel.addImage(
                    image = it, imageType = type
                )
            },
            onImageDeleteClicked = {
                galleryState.removeImage(image = it)
            })
    }
}