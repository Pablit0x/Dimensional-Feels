package com.ps.dimensional_feels.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onDeleteAllClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.happy_days_logo),
                    contentDescription = stringResource(
                        id = R.string.app_logo,
                    ),
                    modifier = Modifier.size(250.dp)
                )
            }
            NavigationDrawerItem(label = {
                Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Image(
                        painterResource(id = R.drawable.google_logo),
                        contentDescription = stringResource(
                            id = R.string.google_logo
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.google_sign_out),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }, selected = false, onClick = onSignOutClicked)

            NavigationDrawerItem(label = {
                Row(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(
                            id = R.string.delete_all_diaries_icon
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.delete_all_diaries),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }, selected = false, onClick = onDeleteAllClicked)
        }
    }, content = content)
}