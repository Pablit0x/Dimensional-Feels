package com.ps.dimensional_feels.presentation.screens.draw

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HdrStrong
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import io.ak1.drawbox.DrawController

@Composable
fun ControlsBar(
    drawController: DrawController,
    onDownloadClick: () -> Unit,
    onSizeClick: () -> Unit,
    undoVisibility: Boolean,
    redoVisibility: Boolean
) {
    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceAround) {
        ControlMenuItems(
            Icons.Default.Download,
            stringResource(id = R.string.save_drawing),
            if (undoVisibility) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
        ) {
            if (undoVisibility) onDownloadClick()
        }
        ControlMenuItems(
            Icons.Default.Undo,
            stringResource(id = R.string.undo),
            if (undoVisibility) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
        ) {
            if (undoVisibility) drawController.unDo()
        }
        ControlMenuItems(
            Icons.Default.Redo,
            stringResource(id = R.string.redo),
            if (redoVisibility) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
        ) {
            if (redoVisibility) drawController.reDo()
        }
        ControlMenuItems(
            Icons.Default.RestartAlt,
            stringResource(id = R.string.restart),
            if (redoVisibility || undoVisibility) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
        ) {
            drawController.reset()
        }
        ControlMenuItems(
            Icons.Default.HdrStrong,
            stringResource(id = R.string.stroke_size),
            MaterialTheme.colorScheme.primary
        ) {
            onSizeClick()
        }
    }
}