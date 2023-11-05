package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.ps.dimensional_feels.presentation.components.TimePickerDialog
import com.ps.dimensional_feels.util.Constants
import com.ps.dimensional_feels.util.PreferencesManager
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onSignOutClicked: () -> Unit,
    onClearDiaryClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    var isDailyReminderEnabled by remember {
        mutableStateOf(
            preferencesManager.getBoolean(
                Constants.IS_DAILY_REMINDER_ENABLED_KEY, false
            )
        )
    }

    var dailyReminderHour by remember {
        mutableIntStateOf(
            preferencesManager.getInt(
                Constants.DAILY_REMINDER_HOUR_KEY, 20
            )
        )
    }

    var dailyReminderMinute by remember {
        mutableIntStateOf(
            preferencesManager.getInt(
                Constants.DAILY_REMINDER_MINUTE_KEY, 0
            )
        )
    }

    val formattedTime = remember(dailyReminderHour, dailyReminderMinute) {
        val localDateTime = LocalTime.of(dailyReminderHour, dailyReminderMinute)
        DateTimeFormatter.ofPattern(Constants.TIME_PATTERN).format(localDateTime).uppercase()
    }

    Column(modifier = modifier) {

        Text(
            text = stringResource(id = R.string.reminder_settings),
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        DailyReminderAlarmCard(alarmTime = formattedTime,
            onDailyReminderSwitchChange = {
                isDailyReminderEnabled = it
                preferencesManager.saveBoolean(Constants.IS_DAILY_REMINDER_ENABLED_KEY, it)
            },
            isDailyReminderEnabled = isDailyReminderEnabled,
            onClick = { showTimePickerDialog = true })

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.account_settings),
            style = MaterialTheme.typography.headlineMedium
        )


        Spacer(modifier = Modifier.height(8.dp))

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

    if (showTimePickerDialog) {

        val timePickerState = rememberTimePickerState(
            initialHour = dailyReminderHour, initialMinute = dailyReminderMinute
        )


        TimePickerDialog(onCancel = { showTimePickerDialog = false }, onConfirm = {
            dailyReminderHour = timePickerState.hour
            dailyReminderMinute = timePickerState.minute
            preferencesManager.saveInt(Constants.DAILY_REMINDER_HOUR_KEY, dailyReminderHour)
            preferencesManager.saveInt(Constants.DAILY_REMINDER_MINUTE_KEY, dailyReminderMinute)
            showTimePickerDialog = false
        }) {
            TimePicker(state = timePickerState)
        }
    }
}