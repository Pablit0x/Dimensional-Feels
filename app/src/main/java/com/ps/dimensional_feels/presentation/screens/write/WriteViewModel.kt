package com.ps.dimensional_feels.presentation.screens.write

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.ps.mongo.database.ImageToDeleteDao
import com.ps.mongo.database.ImageToUploadDao
import com.ps.mongo.database.entity.ImageToDelete
import com.ps.mongo.database.entity.ImageToUpload
import com.ps.mongo.repository.MongoDb
import com.ps.ui.GalleryImage
import com.ps.ui.GalleryState
import com.ps.util.NavigationArguments.WRITE_SCREEN_ARGUMENT_KEY
import com.ps.util.RequestState
import com.ps.util.exceptions.DiaryAlreadyDeletedException
import com.ps.util.fetchImagesFromFirebase
import com.ps.util.model.Diary
import com.ps.util.model.Mood
import com.ps.util.model.getMoodByName
import com.ps.util.model.toRickAndMortyCharacter
import com.ps.util.toRealmInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val imageToDeleteDao: ImageToDeleteDao,
    private val imageToUploadDao: ImageToUploadDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(WriteUiState())
        private set

    val galleryState = GalleryState()

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    private fun getDiaryIdArgument() {
        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARGUMENT_KEY
            )
        )
    }

    private fun fetchSelectedDiary() {
        uiState.selectedDiaryId?.let { selectedId ->
            viewModelScope.launch {
                MongoDb.getSelectedDiary(diaryId = ObjectId(hexString = selectedId)).catch {
                    emit(RequestState.Error(DiaryAlreadyDeletedException()))
                }.collect { diary ->
                    if (diary is RequestState.Success) {
                        setSelectedDiary(diary = diary.data)
                        setMood(
                            mood = getMoodByName(
                                name = diary.data.mood,
                                character = diary.data.character.toRickAndMortyCharacter()
                            )
                        )
                        setTitle(title = diary.data.title)
                        setDescription(description = diary.data.description)

                        fetchImagesFromFirebase(remoteImagePaths = diary.data.images,
                            onImageDownload = { downloadedImage ->
                                galleryState.addImage(
                                    GalleryImage(
                                        image = downloadedImage, remoteImagePath = extractImagePath(
                                            fullImageUrl = downloadedImage.toString()
                                        )
                                    )
                                )
                            })
                    }
                }
            }
        }
    }

    fun upsertDiary(
        diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedDiaryId != null) {
                updateDiary(diary = diary, onSuccess = onSuccess, onError = onError)
            } else {
                insertDiary(diary = diary, onSuccess = onSuccess, onError = onError)
            }
        }
    }

    private suspend fun insertDiary(
        diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        val result = MongoDb.insertDiary(diary = diary.apply {
            if (uiState.updatedDateTime != null) {
                date = uiState.updatedDateTime!!
            }
        })
        if (result is RequestState.Success) {
            uploadImagesToFirebase()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            onError(result.error.message ?: "Unknown error...")
        }
    }

    private suspend fun updateDiary(
        diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        val result = MongoDb.updateDiary(diary = diary.apply {
            _id = ObjectId.invoke(hexString = uiState.selectedDiaryId!!)
            date =
                if (uiState.updatedDateTime != null) uiState.updatedDateTime!! else uiState.selectedDiary!!.date
        })
        if (result is RequestState.Success) {
            uploadImagesToFirebase()
            deleteImagesFromFirebase()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } else if (result is RequestState.Error) {
            onError(result.error.message ?: "Unknown error...")
        }
    }

    fun deleteDiary(
        onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.selectedDiaryId != null) {
                val result =
                    MongoDb.deleteDiary(diaryId = ObjectId.invoke(hexString = uiState.selectedDiaryId!!))
                if (result is RequestState.Success) {
                    withContext(Dispatchers.Main) {
                        uiState.selectedDiary?.let {
                            deleteImagesFromFirebase(it.images)
                        }
                        onSuccess()
                    }
                } else if (result is RequestState.Error) {
                    withContext(Dispatchers.Main) {
                        onError(result.error.message ?: "Unknown error...")
                    }
                }
            }
        }
    }

    private fun uploadImagesToFirebase() {
        val storage = FirebaseStorage.getInstance().reference
        galleryState.images.forEach { galleryImage ->
            val imagePath = storage.child(galleryImage.remoteImagePath)
            imagePath.putFile(galleryImage.image).addOnProgressListener {
                val sessionUri = it.uploadSessionUri
                if (sessionUri != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        imageToUploadDao.addImageToUpload(
                            ImageToUpload(
                                remoteImagePath = galleryImage.remoteImagePath,
                                imageUri = galleryImage.image.toString(),
                                sessionUri = sessionUri.toString()
                            )
                        )
                    }
                }
            }
        }
    }

    fun addImage(image: Uri, imageType: String) {
        val remoteImagePath =
            "images/${FirebaseAuth.getInstance().currentUser?.uid}/${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
        galleryState.addImage(
            GalleryImage(image = image, remoteImagePath = remoteImagePath)
        )
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(title = title)
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(description = description)
    }

    fun setMood(mood: Mood) {
        uiState = uiState.copy(
            mood = mood
        )
    }

    fun setSelectedDiary(diary: Diary) {
        uiState = uiState.copy(
            selectedDiary = diary
        )
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime?) {
        uiState = if (zonedDateTime != null) {
            uiState.copy(
                updatedDateTime = zonedDateTime.toInstant().toRealmInstant()
            )
        } else {
            uiState.copy(
                updatedDateTime = null
            )
        }
    }

    private fun deleteImagesFromFirebase(images: List<String>? = null) {
        val storage = FirebaseStorage.getInstance().reference
        if (images != null) {
            images.forEach { remotePath ->
                storage.child(remotePath).delete().addOnFailureListener { e ->
                    viewModelScope.launch(Dispatchers.IO) {
                        imageToDeleteDao.addImageToDelete(ImageToDelete(remoteImagePath = remotePath))
                    }
                }
            }
        } else {
            galleryState.imagesToBeDeleted.map { it.remoteImagePath }.forEach { remotePath ->
                storage.child(remotePath).delete().addOnFailureListener { e ->
                    viewModelScope.launch(Dispatchers.IO) {
                        imageToDeleteDao.addImageToDelete(ImageToDelete(remoteImagePath = remotePath))
                    }
                }
            }
        }
    }

    private fun extractImagePath(fullImageUrl: String): String {
        val chunks = fullImageUrl.split("%2F")
        val imageName = chunks[2].split("?").first()
        return "images/${Firebase.auth.currentUser?.uid}/$imageName"
    }
}