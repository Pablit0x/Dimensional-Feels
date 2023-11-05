package com.ps.dimensional_feels.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AlarmOff
import androidx.compose.material.icons.outlined.AlarmOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyReminderAlarmCard(
    alarmTime: String,
    onDailyReminderSwitchChange: (Boolean) -> Unit,
    isDailyReminderEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {


    val color = if(isDailyReminderEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline

    OutlinedCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alarmTime,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
                Spacer(modifier = Modifier.height(4.dp))
                ShortReminderDescriptionWithIconRow(
                    isReminderEnabled = isDailyReminderEnabled,
                    color = color
                )
            }
            Switch(checked = isDailyReminderEnabled, onCheckedChange = {
                onDailyReminderSwitchChange(it)
            })
        }
    }
}

@Composable
private fun ShortReminderDescriptionWithIconRow(
    isReminderEnabled: Boolean, color: Color, modifier: Modifier = Modifier
) {
    val icon = if (isReminderEnabled) Icons.Outlined.AlarmOn else Icons.Outlined.AlarmOff
    val description =
        if (isReminderEnabled) stringResource(id = R.string.reminder_on) else stringResource(
            id = R.string.reminder_off
        )
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = null,
            tint = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = description,
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Normal,
            color = color
        )
    }
}