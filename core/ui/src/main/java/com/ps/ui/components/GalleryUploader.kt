package com.ps.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ps.ui.GalleryImage
import com.ps.ui.GalleryState
import com.ps.util.R
import kotlin.math.max

@Composable
fun GalleryUploader(
    modifier: Modifier = Modifier,
    galleryState: GalleryState,
    imageSize: Dp = 60.dp,
    imageShape: CornerBasedShape = Shapes().medium,
    spaceBetween: Dp = 12.dp,
    onAddClicked: () -> Unit,
    onImageSelected: (Uri) -> Unit,
    onImageClicked: (GalleryImage, Int) -> Unit
) {
    val multiplePhotoPicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia(
            maxItems = 8
        ), onResult = { images ->
            images.forEach { image ->
                onImageSelected(image)
            }
        })


    BoxWithConstraints(modifier = modifier) {

        val context = LocalContext.current

        val numberOfVisibleImages by remember {
            derivedStateOf {
                max(a = 0, b = maxWidth.div(spaceBetween + imageSize).toInt().minus(2))
            }
        }

        val remainingImages by remember {
            derivedStateOf {
                galleryState.images.size - numberOfVisibleImages
            }
        }

        Row {
            AddImageButton(imageSize = imageSize, imageShape = imageShape, onClick = {
                onAddClicked()
                multiplePhotoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            })
            Spacer(modifier = Modifier.width(spaceBetween))
            galleryState.images.take(numberOfVisibleImages).forEachIndexed { index, galleryImage ->
                AsyncImage(
                    modifier = Modifier
                        .clip(imageShape)
                        .size(imageSize)
                        .clickable { onImageClicked(galleryImage, index) },
                    model = ImageRequest.Builder(context = context).data(galleryImage.image)
                        .crossfade(true).build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(id = R.string.gallery_image)
                )
                Spacer(modifier = Modifier.width(spaceBetween))
            }
            if (remainingImages > 0) {
                LastImageOverlay(
                    imageSize = imageSize,
                    numberOfRemainingImages = remainingImages,
                    imageShape = imageShape
                )
            }
        }
    }
}