package com.ps.dimensional_feels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.ps.dimensional_feels.navigation.SetupNavGraph
import com.ps.mongo.database.ImageToDeleteDao
import com.ps.mongo.database.ImageToUploadDao
import com.ps.mongo.database.entity.ImageToDelete
import com.ps.mongo.database.entity.ImageToUpload
import com.ps.ui.theme.DimensionalFeelsTheme
import com.ps.util.Constants.APP_ID
import com.ps.util.Screen
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var keepSplashOpened = true

    @Inject
    lateinit var imageToUploadDao: ImageToUploadDao

    @Inject
    lateinit var imageToDeleteDao: ImageToDeleteDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen().setKeepOnScreenCondition { keepSplashOpened }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        FirebaseApp.initializeApp(applicationContext)
        setContent {
            DimensionalFeelsTheme {
                val navController = rememberNavController()
                SetupNavGraph(startDestinationRoute = getStartDestination(),
                    navController = navController,
                    onDataLoaded = { keepSplashOpened = false })
            }
        }
        cleanupCheck(
            scope = lifecycleScope,
            imageToUploadDao = imageToUploadDao,
            imageToDeleteDao = imageToDeleteDao
        )
    }
}


private fun cleanupCheck(
    scope: CoroutineScope, imageToUploadDao: ImageToUploadDao, imageToDeleteDao: ImageToDeleteDao
) {
    scope.launch(Dispatchers.IO + SupervisorJob()) {
        val uploadResult = imageToUploadDao.getAllImages()
        uploadResult.forEach { imageToUpload ->
            retryUploadingImageToFirebase(imageToUpload = imageToUpload, onSuccess = {
                scope.launch(Dispatchers.IO) {
                    imageToUploadDao.deleteImageToUpload(imageId = imageToUpload.id)
                }
            })
        }
        val deleteResult = imageToDeleteDao.getAllImages()
        deleteResult.forEach { imageToDelete ->
            retryDeletingImagesFromFirebase(imageToDelete = imageToDelete, onSuccess = {
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
    imageToUpload: ImageToUpload, onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(
        imageToUpload.remoteImagePath
    ).putFile(
        imageToUpload.imageUri.toUri(), storageMetadata { }, imageToUpload.sessionUri.toUri()
    ).addOnSuccessListener { onSuccess() }
}

fun retryDeletingImagesFromFirebase(
    imageToDelete: ImageToDelete, onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToDelete.remoteImagePath).delete().addOnSuccessListener { onSuccess() }
}