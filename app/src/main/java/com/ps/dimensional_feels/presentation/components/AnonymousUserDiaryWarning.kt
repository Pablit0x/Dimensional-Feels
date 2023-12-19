package com.ps.dimensional_feels.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.compose.Balloon

@Composable
fun AnonymousUserDiaryWarning(
    builder: Balloon.Builder, warningText: String, modifier: Modifier = Modifier
) {

    Balloon(
        builder = builder,
        balloonContent = {
            Text(text = "Now you can edit your profile!")
        }
    ) { balloonWindow ->

        Row(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(30)
                )
                .clickable {
                    balloonWindow.showAlignBottom()
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = warningText, color = MaterialTheme.colorScheme.onError)

            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}