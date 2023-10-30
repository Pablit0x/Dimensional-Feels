package com.ps.dimensional_feels.navigation

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Mood
import com.ps.dimensional_feels.model.getMoodByPosition
import com.ps.dimensional_feels.model.toRickAndMortyCharacter
import com.ps.dimensional_feels.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY_DIARY_ID
import com.ps.dimensional_feels.navigation.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY_DRAWING_URI
import com.ps.dimensional_feels.presentation.components.CustomAlertDialog
import com.ps.dimensional_feels.presentation.screens.auth.AuthenticationScreen
import com.ps.dimensional_feels.presentation.screens.auth.AuthenticationViewModel
import com.ps.dimensional_feels.presentation.screens.draw.DrawScreen
import com.ps.dimensional_feels.presentation.screens.home.HomeScreen
import com.ps.dimensional_feels.presentation.screens.home.HomeViewModel
import com.ps.dimensional_feels.presentation.screens.write.WriteScreen
import com.ps.dimensional_feels.presentation.screens.write.WriteViewModel
import com.ps.dimensional_feels.util.RequestState
import com.ps.dimensional_feels.util.saveImage
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
        }, onNavigateToDraw = {
            navController.navigate(Screen.Draw.route)
        })
        drawRoute(onBackPressed = {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                WRITE_SCREEN_ARGUMENT_KEY_DRAWING_URI, null
            )
            navController.popBackStack()
        }, onNavigateBackAndPassUri = { uri ->
            if (uri != null) {
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    WRITE_SCREEN_ARGUMENT_KEY_DRAWING_URI, uri.toString()
                )
            }
            navController.popBackStack()
        })
    }

}


fun NavGraphBuilder.authenticationRoute(navigateHome: () -> Unit, onDataLoaded: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val context = LocalContext.current
        val viewModel = hiltViewModel<AuthenticationViewModel>()
        val googleLoadingState by viewModel.googleLoadingState
        val guestLoadingState by viewModel.guestLoadingState
        val authenticated by viewModel.authenticated
        val firebaseAuth = viewModel.firebaseAuth
        val oneTapSignInState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }

        AuthenticationScreen(
            firebaseAuth = firebaseAuth,
            oneTapSignInState = oneTapSignInState,
            messageBarState = messageBarState,
            isGoogleLoading = googleLoadingState,
            isGuestLoading = guestLoadingState,
            onGuestSignInClicked = {
                viewModel.setGuestLoading(isLoading = true)
                viewModel.signInAnonymously(onSuccess = {
                    viewModel.signInWithMongoAtlas(null, onSuccess = {
                        messageBarState.addSuccess(message = context.getString(R.string.success))
                    }, onError = { errorMsg ->
                        messageBarState.addError(exception = Exception(errorMsg))
                    })
                }, onError = {
                    messageBarState.addError(it)
                    viewModel.setGuestLoading(false)
                })
            },
            authenticated = authenticated,
            onGoogleSignInClicked = {
                oneTapSignInState.open()
                viewModel.setGoogleLoading(isLoading = true)
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
                viewModel.setGoogleLoading(false)
            },
            onDialogDismissed = { errorMsg ->
                messageBarState.addError(Exception(errorMsg))
                viewModel.setGoogleLoading(isLoading = false)
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
            },
            onSearch = {
                viewModel.getDiaries(searchText = it)
            },
            onSearchReset = {
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

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit, onNavigateToDraw: () -> Unit
) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY_DIARY_ID) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ) { entry ->


        val drawingUri = entry.savedStateHandle.get<String>(WRITE_SCREEN_ARGUMENT_KEY_DRAWING_URI)
        var isLoading by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val viewModel = hiltViewModel<WriteViewModel>()
        val galleryState = viewModel.galleryState

        // Page count reflects number of moods
        val pagerState = rememberPagerState(
            pageCount = { Mood.MOOD_COUNT },
        )
        val uiState = viewModel.uiState
        val pageNumber by remember {
            derivedStateOf {
                pagerState.currentPage
            }
        }

        LaunchedEffect(Unit) {
            if (drawingUri != null) {
                viewModel.addImage(image = drawingUri.toUri(), "png")
            }
        }


        WriteScreen(
            isLoading = isLoading,
            uiState = uiState,
            galleryState = galleryState,
            pagerState = pagerState,
            onBackPressed = onBackPressed,
            onDeleteConfirmed = {
                viewModel.deleteDiary(onSuccess = {
                    onBackPressed()
                }, onError = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.deleting_error_occurred),
                        Toast.LENGTH_LONG
                    ).show()
                })
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
                }, onLoading = {
                    isLoading = true
                }, onSuccess = {
                    isLoading = false
                    onBackPressed()
                }, onError = { isLoading = false })
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
            },
            onCharacterChange = { viewModel.setCharacter(it) },
            onNavigateToDraw = onNavigateToDraw
        )
    }
}

fun NavGraphBuilder.drawRoute(onBackPressed: () -> Unit, onNavigateBackAndPassUri: (Uri?) -> Unit) {
    composable(route = Screen.Draw.route) {
        val context = LocalContext.current

        DrawScreen(onBackPressed = onBackPressed, onSavedPressed = {
            val uri = context.saveImage(bitmap = it)
            onNavigateBackAndPassUri(uri)
        })
    }
}