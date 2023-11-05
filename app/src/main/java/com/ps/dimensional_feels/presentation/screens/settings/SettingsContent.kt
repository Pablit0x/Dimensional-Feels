package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.presentation.components.DailyReminderAlarmCard
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

        DailyReminderAlarmCard(alarmTime = "12:30",
            onDailyReminderSwitchChange = {
                isDailyReminderEnabled = it
                preferencesManager.saveBoolean(Constants.IS_DAILY_REMINDER_ENABLED_KEY, it)
            },
            isDailyReminderEnabled = isDailyReminderEnabled,
            onClick = { /*TODO*/ })

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.account_settings),
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(
            optionText = stringResource(id = R.string.google_sign_out),
            optionIcon = Icons.Outlined.ExitToApp,
            onClick = onSignOutClicked
        )

        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(
            optionText = stringResource(id = R.string.clear_diary),
            optionIcon = Icons.Outlined.DeleteOutline,
            onClick = onClearDiaryClicked,
            backgroundColor = MaterialTheme.colorScheme.error,
            onBackgroundColor = MaterialTheme.colorScheme.onError
        )

        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(
            optionText = stringResource(id = R.string.delete_account),
            optionIcon = Icons.Outlined.Close,
            onClick = onDeleteAccountClicked,
            backgroundColor = MaterialTheme.colorScheme.error,
            onBackgroundColor = MaterialTheme.colorScheme.onError
        )

    }
}