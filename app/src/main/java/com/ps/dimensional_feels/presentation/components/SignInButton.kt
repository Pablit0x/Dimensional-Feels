package com.ps.dimensional_feels.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R

@Composable
fun SignInButton(
    primaryText: String,
    iconRes: Any,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    secondaryText: String = stringResource(id = R.string.please_wait),
    shape: Shape = Shapes().large,
    borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderStrokeWidth: Dp = 1.dp,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    var buttonText by remember { mutableStateOf(primaryText) }

    LaunchedEffect(key1 = isLoading) {
        buttonText = if (isLoading) secondaryText else primaryText
    }

    Surface(
        modifier = modifier.clickable(enabled = !isLoading) {
            onClick()
        },
        shape = shape,
        border = BorderStroke(width = borderStrokeWidth, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(12.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300, easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (iconRes is ImageVector) {
                Icon(
                    imageVector = iconRes,
                    contentDescription = stringResource(id = R.string.login_provider_logo),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            } else if (iconRes is Int) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = stringResource(id = R.string.login_provider_logo),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = buttonText, style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
            )

            if (isLoading) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}