package com.ps.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ps.ui.theme.Elevation
import com.ps.util.R
import com.ps.util.fetchImagesFromFirebase
import com.ps.util.model.Diary
import com.ps.util.model.toRickAndMortyCharacter
import com.ps.util.toInstant


@Composable
fun DiaryHolder(
    diary: Diary, onClick: (String) -> Unit
) {
    val localDensity = LocalDensity.current
    val context = LocalContext.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    var showGallery by remember { mutableStateOf(false) }
    var galleryLoading by remember { mutableStateOf(false) }
    val downloadedImages = remember { mutableStateListOf<Uri>() }

    LaunchedEffect(key1 = showGallery) {
        if (showGallery && downloadedImages.isEmpty()) {
            galleryLoading = true
            fetchImagesFromFirebase(remoteImagePaths = diary.images, onImageDownload = { imageUri ->
                downloadedImages.add(imageUri)
            }, onImageDownloadFailed = { e ->
                Toast.makeText(
                    context, e.message ?: "Unknown error occurred...", Toast.LENGTH_SHORT
                ).show()
                galleryLoading = false
                showGallery = false
            }, onReadyToDisplay = {
                galleryLoading = false
            })
        }
    }

    Row(modifier = Modifier.clickable(indication = null, interactionSource = remember {
        MutableInteractionSource()
    }) { onClick(diary._id.toString()) }) {
        Spacer(modifier = Modifier.width(14.dp))
        Surface(modifier = Modifier
            .width(2.dp)
            .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1,
            content = {})
        Spacer(modifier = Modifier.width(20.dp))
        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                }, tonalElevation = Elevation.Level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DiaryHeader(
                    character = diary.character.toRickAndMortyCharacter(),
                    moodName = diary.mood,
                    time = diary.date.toInstant(),
                    title = diary.title
                )
                Text(
                    modifier = Modifier.padding(14.dp),
                    text = diary.description,
                    style = TextStyle.Default.copy(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                if (diary.images.isNotEmpty()) {
                    TextButton(onClick = { showGallery = !showGallery }) {
                        Text(
                            text = if (showGallery && galleryLoading) stringResource(id = R.string.loading) else if (showGallery) stringResource(
                                id = R.string.hide_gallery
                            ) else stringResource(
                                id = R.string.show_gallery
                            ), style = TextStyle.Default.copy(
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }
                }
                AnimatedVisibility(
                    visible = showGallery && !galleryLoading, enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(all = 14.dp)) {
                        Gallery(images = downloadedImages)
                    }
                }
            }
        }
    }
}