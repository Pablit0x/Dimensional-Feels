package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.presentation.components.SettingsCardItem
import com.ps.dimensional_feels.util.Constants
import com.ps.dimensional_feels.util.PreferencesManager


@Composable
fun SettingsContent(
    onSignOutClicked: () -> Unit,
    onClearDiaryClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    var isDailyReminderEnabled by remember {
        mutableStateOf(
            preferencesManager.getBoolean(
                Constants.IS_DAILY_REMINDER_ENABLED_KEY, false
            )
        )
    }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(id = R.string.daily_reminder))
            Switch(checked = isDailyReminderEnabled,
                onCheckedChange = {
                    isDailyReminderEnabled = it
                    preferencesManager.saveBoolean(Constants.IS_DAILY_REMINDER_ENABLED_KEY, it)
                })
        }

        AnimatedVisibility(visible = isDailyReminderEnabled) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = "12:30")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.account_settings),
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(
            optionText = stringResource(id = R.string.google_sign_out),
            optionIcon = Icons.Default.ExitToApp,
            onClick = onSignOutClicked
        )

        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(
            optionText = stringResource(id = R.string.clear_diary),
            optionIcon = Icons.Default.Delete,
            onClick = onClearDiaryClicked,
            backgroundColor = MaterialTheme.colorScheme.error,
            onBackgroundColor = MaterialTheme.colorScheme.onError
        )

        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(
            optionText = stringResource(id = R.string.delete_account),
            optionIcon = Icons.Default.Close,
            onClick = onDeleteAccountClicked,
            backgroundColor = MaterialTheme.colorScheme.error,
            onBackgroundColor = MaterialTheme.colorScheme.onError
        )

    }
}