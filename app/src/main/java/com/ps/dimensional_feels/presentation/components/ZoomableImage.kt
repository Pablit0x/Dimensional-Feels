package com.ps.dimensional_feels.presentation.components

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.GalleryImage

@Composable
fun ZoomableImage(
    selectedGalleryImage: GalleryImage
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var scale by remember { mutableStateOf(1f) }

    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                scale = maxOf(a = 1f, minOf(scale * zoom, b = 5f))
                val maxX = (size.width * (scale - 1)) / 2
                val minX = -maxX
                offsetX = maxOf(minX, minOf(maxX, offsetX + pan.x))
                val maxY = (size.height * (scale - 1)) / 2
                val minY = -maxY
                offsetY = maxOf(minY, minOf(maxY, offsetY + pan.y))
            }
        }) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = maxOf(0.5f, minOf(3f, scale)),
                    scaleY = maxOf(0.5f, minOf(3f, scale)),
                    translationX = offsetX,
                    translationY = offsetY
                ),
            model = ImageRequest.Builder(context).data(selectedGalleryImage.image.toString())
                .crossfade(true).build(),
            contentDescription = stringResource(id = R.string.gallery_image)
        )
    }
}