package com.ps.dimensional_feels.presentation.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ps.dimensional_feels.R
import kotlin.math.max

@Composable
fun Gallery(
    modifier: Modifier = Modifier,
    images: List<String>,
    imageSize: Dp = 40.dp,
    spaceBetween: Dp = 10.dp,
    imageShape: CornerBasedShape = Shapes().small
) {
    val context = LocalContext.current

    BoxWithConstraints(modifier = modifier) {
        val numberOfVisibleImages by remember {
            derivedStateOf {
                max(a = 0, b = maxWidth.div(spaceBetween + imageSize).toInt().minus(1))
            }
        }
        val remainingImages by remember {
            derivedStateOf {
                images.size - numberOfVisibleImages
            }
        }

        Row {
            images.take(numberOfVisibleImages).forEach { image ->
                AsyncImage(
                    modifier = Modifier
                        .clip(imageShape)
                        .size(imageSize),
                    model = ImageRequest.Builder(context = context).data(image).crossfade(true)
                        .build(),
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