package com.ps.dimensional_feels.presentation.components

import android.media.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCardItem(
    optionText: String, optionIcon: Any, modifier: Modifier = Modifier, onClick: () -> Unit
) {

    OutlinedCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (optionIcon is Painter) {
                    Icon(painter = optionIcon, contentDescription = null)
                } else if (optionIcon is ImageVector) {
                    Icon(imageVector = optionIcon, contentDescription = null)
                }
                Text(
                    text = optionText,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null
            )
        }
    }
}


@Preview
@Composable
fun SettingCardItemPrev() {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsCardItem(
            optionText = "Exit App",
            optionIcon = Icons.Default.ExitToApp,
            onClick = {},
        )
    }
}