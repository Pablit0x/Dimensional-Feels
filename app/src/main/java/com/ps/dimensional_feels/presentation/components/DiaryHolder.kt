package com.ps.dimensional_feels.presentation.components

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.toRickAndMortyCharacter
import com.ps.dimensional_feels.presentation.theme.Elevation
import com.ps.dimensional_feels.util.fetchImagesFromFirebase
import com.ps.dimensional_feels.util.toInstant

@Composable
fun DiaryHolder(
    diary: Diary, onClick: (String) -> Unit
) {
    val localDensity = LocalDensity.current
    val context = LocalContext.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    var showGallery by remember { mutableStateOf(true) }
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

    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = Modifier.clickable(
        indication = null, interactionSource = interactionSource
    ) { onClick(diary._id.toString()) }) {
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
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    text = diary.description,
                    style = TextStyle.Default.copy(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                AnimatedVisibility(
                    visible = downloadedImages.isNotEmpty(), enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Column(modifier = Modifier.padding(all = 14.dp)) {
                        if (galleryLoading) {
                            Text(text = stringResource(id = R.string.loading))
                        } else {
                            Gallery(images = downloadedImages)
                        }
                    }
                }
            }
        }
    }
}