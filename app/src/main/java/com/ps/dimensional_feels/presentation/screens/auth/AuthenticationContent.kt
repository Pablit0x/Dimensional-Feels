package com.ps.dimensional_feels.presentation.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person3
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.presentation.components.AnimatedBorderCard
import com.ps.dimensional_feels.presentation.components.SignInButton
import com.ps.dimensional_feels.presentation.theme.PortalGreen
import com.ps.dimensional_feels.presentation.theme.PortalPurple

@Composable
fun AuthenticationContent(
    isGoogleLoading: Boolean,
    onSignInWithGoogleButtonClicked: () -> Unit,
    isAnonymousLoading: Boolean,
    onAnonymousSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth()
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedBorderCard(
                modifier = Modifier.weight(10f), shape = RoundedCornerShape(15),
                borderWidth = 3.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        PortalPurple,
                        PortalGreen
                    )
                )
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = R.drawable.rick_and_morty,
                        contentDescription = null,
                        modifier = Modifier.size(180.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(id = R.string.welcome_back),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.please_sign_in_to_continue),
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.40f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(
                modifier = Modifier.weight(3f), verticalArrangement = Arrangement.Bottom
            ) {

                SignInButton(
                    primaryText = stringResource(id = R.string.sign_in_with_google),
                    iconRes = Icons.Outlined.Person3,
                    onClick = onAnonymousSignIn,
                    isLoading = isAnonymousLoading
                    )

                Spacer(modifier = Modifier.height(12.dp))
                SignInButton(
                    primaryText = stringResource(id = R.string.sign_in_with_google),
                    iconRes = R.drawable.google_logo,
                    isLoading = isGoogleLoading,
                    onClick = onSignInWithGoogleButtonClicked
                )
            }
        }
    }
}