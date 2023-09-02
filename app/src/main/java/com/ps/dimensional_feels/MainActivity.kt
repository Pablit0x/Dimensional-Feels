package com.ps.dimensional_feels

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.ps.dimensional_feels.data.database.ImageToDeleteDao
import com.ps.dimensional_feels.data.database.ImageToUploadDao
import com.ps.dimensional_feels.data.database.entity.ImageToDelete
import com.ps.dimensional_feels.data.database.entity.ImageToUpload
import com.ps.dimensional_feels.navigation.NavGraph
import com.ps.dimensional_feels.navigation.Screen
import com.ps.dimensional_feels.presentation.theme.DimensionalFeelsTheme
import com.ps.dimensional_feels.util.Constants.APP_ID
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val isPermissionGranted = mutableStateOf(false)


    private var keepSplashOpened = true

    @Inject
    lateinit var firebaseStorage: FirebaseStorage

    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao

    @Inject
    lateinit var imageToDeleteDao: ImageToDeleteDao

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val permissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            isPermissionGranted.value = permissions.values.all { it }
//        }
//
//
//        permissionLauncher.launch(
//            arrayOf(
//                Manifest.permission.READ_MEDIA_IMAGES,
//                Manifest.permission.READ_MEDIA_VIDEO,
//                )
//        )

        installSplashScreen().setKeepOnScreenCondition { keepSplashOpened }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(applicationContext)
        setContent {
            DimensionalFeelsTheme {
                val navController = rememberNavController()
                NavGraph(startDestinationRoute = getStartDestination(),
                    navController = navController,
                    onDataLoaded = { keepSplashOpened = false })
            }
        }
        cleanupCheck(
            firebaseStorage = firebaseStorage,
            scope = lifecycleScope,
            imageToUploadDao = imageToUploadDao,
            imageToDeleteDao = imageToDeleteDao
        )
    }
}


private fun cleanupCheck(
    firebaseStorage: FirebaseStorage,
    scope: CoroutineScope,
    imageToUploadDao: ImageToUploadDao,
    imageToDeleteDao: ImageToDeleteDao
) {
    scope.launch(Dispatchers.IO + SupervisorJob()) {
        val uploadResult = imageToUploadDao.getAllImages()
        uploadResult.forEach { imageToUpload ->
            retryUploadingImageToFirebase(firebaseStorage = firebaseStorage,
                imageToUpload = imageToUpload,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imageToUploadDao.deleteImageToUpload(imageId = imageToUpload.id)
                    }
                })
        }
        val deleteResult = imageToDeleteDao.getAllImages()
        deleteResult.forEach { imageToDelete ->
            retryDeletingImageFromFirebase(firebaseStorage = firebaseStorage,
                imageToDelete = imageToDelete,
                onSuccess = {
                    scope.launch(Dispatchers.IO) {
                        imageToDeleteDao.cleanupImage(imageId = imageToDelete.id)
                    }
                })
        }
    }
}

private fun getStartDestination(): String {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) Screen.Home.route else Screen.Authentication.route
}

fun retryUploadingImageToFirebase(
    firebaseStorage: FirebaseStorage, imageToUpload: ImageToUpload, onSuccess: () -> Unit
) {
    val storage = firebaseStorage.reference

    try {
        storage.child(imageToUpload.remoteImagePath).putFile(
            imageToUpload.imageUri.toUri(), storageMetadata { }, imageToUpload.sessionUri.toUri()
        )
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        onSuccess()
    }
}

fun retryDeletingImageFromFirebase(
    firebaseStorage: FirebaseStorage, imageToDelete: ImageToDelete, onSuccess: () -> Unit
) {
    val storage = firebaseStorage.reference
    storage.child(imageToDelete.remoteImagePath).delete().addOnSuccessListener { onSuccess() }
}