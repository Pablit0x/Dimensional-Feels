package com.ps.dimensional_feels.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.GalleryImage
import com.ps.dimensional_feels.model.GalleryState
import com.ps.dimensional_feels.model.RickAndMortyCharacters
import com.ps.dimensional_feels.model.getMoodByName
import com.ps.dimensional_feels.model.getPositionByMood
import com.ps.dimensional_feels.model.toRickAndMortyCharacter
import com.ps.dimensional_feels.presentation.components.EmptyPage
import com.ps.dimensional_feels.presentation.components.ZoomableImage
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    isLoading: Boolean,
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
    onNavigateToDraw: () -> Unit,
    onImageSelected: (Uri) -> Unit,
    onImageDeleteClicked: (GalleryImage) -> Unit,
    onCharacterChange: (RickAndMortyCharacters) -> Unit,
) {

    var selectedImageIndex by remember { mutableIntStateOf(0) }
    val imagePagerState = rememberPagerState(
        pageCount = { galleryState.images.size }
    )

    var selectedGalleryImage by remember { mutableStateOf<GalleryImage?>(null) }


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
                onDateTimeUpdated = onDateTimeUpdated,
                onNavigateToDraw = onNavigateToDraw
            )
        } else {
            ImageTopBar(title = "${stringResource(id = R.string.image)} ${imagePagerState.currentPage + 1}",
                onBackClicked = { selectedGalleryImage = null },
                onDeleteClicked = {
                    val index = galleryState.images.indexOf(selectedGalleryImage!!)
                    onImageDeleteClicked(selectedGalleryImage!!)
                    selectedGalleryImage = if (galleryState.images.isNotEmpty()) {
                        if (index >= galleryState.images.size) {
                            galleryState.images[index - 1]
                        } else {
                            galleryState.images[index]
                        }
                    } else {
                        null
                    }
                })
        }
    }, content = { padding ->

        if (isLoading) {
            EmptyPage(
                modifier = Modifier.padding(padding),
                showLoading = true,
                title = stringResource(id = R.string.save),
                description = stringResource(id = R.string.saving_note)
            )
        } else {
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
                        selectedImageIndex = index
                    },
                    onChangeCharacter = {
                        onCharacterChange(it)
                    })
            }

            AnimatedVisibility(
                visible = selectedGalleryImage != null, enter = fadeIn(), exit = fadeOut()
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ZoomableImage(pagerState = imagePagerState,
                        selectedGalleryImageId = selectedImageIndex,
                        galleryImages = galleryState.images.toList(),
                        onPageChange = {
                            selectedGalleryImage = galleryState.images[imagePagerState.currentPage]
                        })
                }
            }
        }
    })
}