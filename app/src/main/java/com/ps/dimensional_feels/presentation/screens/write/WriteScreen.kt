package com.ps.dimensional_feels.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.ps.dimensional_feels.R
import com.ps.ui.GalleryImage
import com.ps.ui.GalleryState
import com.ps.ui.components.ZoomableImage
import com.ps.util.model.Diary
import com.ps.util.model.getMoodByName
import com.ps.util.model.getPositionByMood
import com.ps.util.model.toRickAndMortyCharacter
import java.time.ZonedDateTime

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: WriteUiState,
    galleryState: GalleryState,
    pagerState: PagerState,
    onBackPressed: () -> Unit,
    moodName: () -> String,
    onDeleteConfirmed: () -> Unit,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSavedClicked: (Diary) -> Unit,
    onDateTimeUpdated: (ZonedDateTime?) -> Unit,
    onImageSelected: (Uri) -> Unit,
    onImageDeleteClicked: (GalleryImage) -> Unit
) {

    var selectedGalleryImage by remember { mutableStateOf<GalleryImage?>(null) }
    var selectedImageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = uiState.mood) {
        val mood = getMoodByName(
            name = uiState.mood.name, character = uiState.characters.name.toRickAndMortyCharacter()
        )
        pagerState.scrollToPage(getPositionByMood(mood))
    }

    Scaffold(topBar = {
        if (selectedGalleryImage == null) {
            WriteTopBar(
                selectedDiary = uiState.selectedDiary,
                onBackPressed = onBackPressed,
                onDeleteConfirmed = onDeleteConfirmed,
                moodName = moodName,
                onDateTimeUpdated = onDateTimeUpdated
            )
        } else {
            ImageTopBar(title = "${stringResource(id = R.string.image)} $selectedImageIndex",
                onBackClicked = { selectedGalleryImage = null },
                onDeleteClicked = {
                    onImageDeleteClicked(selectedGalleryImage!!)
                    selectedGalleryImage = null
                })
        }
    }, content = { padding ->
        AnimatedVisibility(
            visible = selectedGalleryImage == null, enter = fadeIn(), exit = fadeOut()
        ) {
            WriteContent(uiState = uiState,
                galleryState = galleryState,
                paddingValues = padding,
                pagerState = pagerState,
                onTitleChanged = onTitleChanged,
                onDescriptionChanged = onDescriptionChanged,
                onSavedClicked = onSavedClicked,
                onImageSelected = onImageSelected,
                onImageClicked = { galleryImage, index ->
                    selectedGalleryImage = galleryImage
                    selectedImageIndex = index + 1
                })
        }

        AnimatedVisibility(
            visible = selectedGalleryImage != null, enter = fadeIn(), exit = fadeOut()
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (selectedGalleryImage != null) {
                    ZoomableImage(selectedGalleryImage = selectedGalleryImage!!)
                }
            }
        }
    })
}