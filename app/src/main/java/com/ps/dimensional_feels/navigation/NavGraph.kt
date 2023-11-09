package com.ps.dimensional_feels.navigation

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
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
import com.ps.dimensional_feels.presentation.screens.settings.SettingsScreen
import com.ps.dimensional_feels.presentation.screens.settings.SettingsViewModel
import com.ps.dimensional_feels.presentation.screens.write.WriteScreen
import com.ps.dimensional_feels.presentation.screens.write.WriteViewModel
import com.ps.dimensional_feels.util.RequestState
import com.ps.dimensional_feels.util.saveImage
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    startDestinationRoute: String,
    navController: NavHostController,
    drawerState: DrawerState,
    onDataLoaded: () -> Unit
) {
    NavHost(navController = navController, startDestination = startDestinationRoute) {
        authenticationRoute(navigateHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        }, onDataLoaded = onDataLoaded)
        homeRoute(navigateToWriteWithArgs = {
            navController.navigate(Screen.Write.passDiaryId(it))
        },
            navigateToWrite = {
                navController.navigate(Screen.Write.route)
            },
            navigateAuth = {
                navController.popBackStack()
                navController.navigate(Screen.Authentication.route)
            },
            onDataLoaded = onDataLoaded,
            drawerState = drawerState,
            navigateSettings = { navController.navigate(Screen.Settings.route) })
        writeRoute(onNavigateHome = {
            navController.navigate(Screen.Home.route)
        }, onBackPressed = {
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
        settingsRoute(navigateHome = { navController.navigate(Screen.Home.route) }, navigateAuth = {
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)
        }, drawerState = drawerState
        )
    }

}


fun NavGraphBuilder.authenticationRoute(navigateHome: () -> Unit, onDataLoaded: () -> Unit) {
    composable(route = Screen.Authentication.route) {
        val context = LocalContext.current
        val viewModel = hiltViewModel<AuthenticationViewModel>()
        val googleLoadingState by viewModel.googleLoadingState
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
    navigateToWriteWithArgs: (String) -> Unit,
    navigateToWrite: () -> Unit,
    navigateAuth: () -> Unit,
    navigateSettings: () -> Unit,
    onDataLoaded: () -> Unit,
    drawerState: DrawerState
) {
    composable(route = Screen.Home.route) {
        val viewModel = hiltViewModel<HomeViewModel>()
        val diaries by viewModel.diaries
        val firebaseStorage = viewModel.firebaseStorage
        var isSignOutDialogOpen by remember { mutableStateOf(false) }
        var isDeleteAllDialogOpen by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = diaries) {
            if (diaries !is RequestState.Loading) {
                onDataLoaded()
            }
        }

        HomeScreen(firebaseStorage = firebaseStorage,
            diaries = diaries,
            drawerState = drawerState,
            onDeleteAllClicked = { isDeleteAllDialogOpen = true },
            onSignOutClicked = { isSignOutDialogOpen = true },
            onSettingsClicked = navigateSettings,
            onMenuClicked = { scope.launch { drawerState.open() } },
            onNavigateToWriteWithArgs = navigateToWriteWithArgs,
            onNavigateToWrite = navigateToWrite,
            dateIsSelected = viewModel.dateIsSelected,
            onDateSelected = { selectedTime ->
                viewModel.getDiaries(zonedDateTime = selectedTime)
            },
            onDateReset = {
                viewModel.getDiaries()
            },
            onSearch = { searchTerm ->
                viewModel.getDiaries(searchText = searchTerm)
            },
            onSearchReset = {
                viewModel.getDiaries()
            })

        CustomAlertDialog(title = stringResource(id = R.string.google_sign_out),
            message = stringResource(
                id = R.string.sign_out_message
            ),
            isOpen = isSignOutDialogOpen,
            onCloseDialog = { isSignOutDialogOpen = false },
            onConfirmClicked = {
                viewModel.logOut(navigateToAuth = { navigateAuth() })
                scope.launch { drawerState.close() }
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun NavGraphBuilder.writeRoute(
    onNavigateHome: () -> Unit, onBackPressed: () -> Unit, onNavigateToDraw: () -> Unit
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
                    onNavigateHome()
                }, onError = { isLoading = false })
            },
            onDateTimeUpdated = { selectedTime ->
                viewModel.updateDateTime(zonedDateTime = selectedTime)
            },
            onImageSelected = { imageUri ->
                val type = context.contentResolver.getType(imageUri)?.split("/")?.last() ?: "jpg"
                viewModel.addImage(
                    image = imageUri, imageType = type
                )
            },
            onImageDeleteClicked = { galleryImage ->
                galleryState.removeImage(image = galleryImage)
            },
            onCharacterChange = { selectedCharacter ->
                viewModel.setCharacter(selectedCharacter)
            },
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

fun NavGraphBuilder.settingsRoute(
    navigateAuth: () -> Unit, navigateHome: () -> Unit, drawerState: DrawerState
) {
    composable(route = Screen.Settings.route) {

        val viewModel = hiltViewModel<SettingsViewModel>()
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        var isSignOutDialogOpen by remember { mutableStateOf(false) }
        var isClearDiaryDialogOpen by remember { mutableStateOf(false) }
        var isDeleteAccountDialogOpen by remember{ mutableStateOf(false) }
        val isDailyReminderEnabled = viewModel.isDailyReminderEnabled
        val dailyReminderTime = viewModel.dailyReminderTime



        SettingsScreen(
            drawerState = drawerState,
            onClearDiaryClicked = { isClearDiaryDialogOpen = true },
            onSignOutClicked = { isSignOutDialogOpen = true },
            onDeleteAccountClicked = {isDeleteAccountDialogOpen = true},
            onHomeClicked = navigateHome,
            onAlarmCanceled = viewModel::cancelAlarm,
            onAlarmScheduled = viewModel::scheduleAlarm,
            onUpdateReminderStatusPrefs = viewModel::updateReminderStatusPrefs,
            onUpdateReminderTimePrefs = viewModel::updateReminderTimePrefs,
            isDailyReminderEnabled = isDailyReminderEnabled,
            dailyReminderTime = dailyReminderTime,
        )


        CustomAlertDialog(title = stringResource(id = R.string.google_sign_out),
            message = stringResource(
                id = R.string.sign_out_message
            ),
            isOpen = isSignOutDialogOpen,
            onCloseDialog = { isSignOutDialogOpen = false },
            onConfirmClicked = {
                viewModel.logOut(navigateToAuth = navigateAuth)
                scope.launch { drawerState.close() }
            })

        CustomAlertDialog(title = stringResource(id = R.string.delete_account) + " ⚠️",
            message = stringResource(
                id = R.string.delete_account_message
            ),
            isOpen = isDeleteAccountDialogOpen,
            onCloseDialog = { isDeleteAccountDialogOpen = false },
            onConfirmClicked = {
                viewModel.deleteAccount(onSuccess = {
                    Toast.makeText(context, context.getString(R.string.account_deleted_successfully), Toast.LENGTH_SHORT).show()
                    navigateAuth()
                }, onError = {
                    Toast.makeText(
                        context,
                        it.message ?: context.getString(R.string.unknown_error),
                        Toast.LENGTH_SHORT
                    ).show()
                })
            })


        CustomAlertDialog(title = stringResource(id = R.string.clear_diary),
            message = stringResource(
                id = R.string.delete_all_diaries_message
            ),
            isOpen = isClearDiaryDialogOpen,
            onCloseDialog = { isClearDiaryDialogOpen = false },
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
    }
}