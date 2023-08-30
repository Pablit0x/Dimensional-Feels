package com.ps.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp

@Composable
fun LastImageOverlay(
    imageSize: Dp, numberOfRemainingImages: Int, imageShape: CornerBasedShape
) {
    Box(contentAlignment = Alignment.Center) {
        Surface(modifier = Modifier
            .clip(imageShape)
            .size(imageSize),
            color = MaterialTheme.colorScheme.primaryContainer,
            content = {})
        Text(
            text = "+${numberOfRemainingImages}", style = TextStyle.Default.copy(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Medium
            ), color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}