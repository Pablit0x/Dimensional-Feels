package com.ps.dimensional_feels.presentation.screens.draw

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import com.ps.dimensional_feels.util.convertToOldColor
import io.ak1.drawbox.DrawBox
import io.ak1.drawbox.rememberDrawController

@Composable
fun DrawingContent(onSavePressed: (Bitmap) -> Unit) {
    var undoVisibility by remember { mutableStateOf(false) }
    var redoVisibility by remember { mutableStateOf(false) }
    var colorBarVisibility by remember { mutableStateOf(false) }
    var sizeBarVisibility by remember { mutableStateOf(false) }
    val bg = MaterialTheme.colorScheme.background
    val onBg = MaterialTheme.colorScheme.onBackground
    var currentColor by remember { mutableStateOf(onBg) }
    var currentBgColor by remember { mutableStateOf(bg) }
    var currentSize by remember { mutableIntStateOf(10) }
    val drawController = rememberDrawController()

    drawController.changeColor(currentColor)

    Box {
        Column {
            DrawBox(drawController = drawController,
                backgroundColor = currentBgColor,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f, fill = false),
                bitmapCallback = { imageBitmap, error ->
                    imageBitmap?.let {
                        onSavePressed(it.asAndroidBitmap())
                    }
                }) { undoCount, redoCount ->
                sizeBarVisibility = false
                colorBarVisibility = false
                undoVisibility = undoCount != 0
                redoVisibility = redoCount != 0
            }

            ControlsBar(
                drawController = drawController,
                {
                    drawController.saveBitmap()
                },
                {
                    sizeBarVisibility = !sizeBarVisibility
                    colorBarVisibility = false
                },
                undoVisibility = undoVisibility,
                redoVisibility = redoVisibility
            )
            CustomSeekbar(
                isVisible = sizeBarVisibility,
                progress = currentSize,
                progressColor = MaterialTheme.colorScheme.primary.convertToOldColor(),
                thumbColor = currentColor.convertToOldColor()
            ) {
                currentSize = it
                drawController.changeStrokeWidth(it.toFloat())
            }
        }

    }
}

