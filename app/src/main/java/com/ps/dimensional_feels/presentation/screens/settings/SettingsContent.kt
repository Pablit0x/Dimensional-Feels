package com.ps.dimensional_feels.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.presentation.components.SettingsCardItem

@Composable
fun SettingsContent(
    onSignOutClicked: () -> Unit,
    onClearDiaryClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDailyReminderEnabled by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(id = R.string.daily_reminder))
            Switch(checked = isDailyReminderEnabled,
                onCheckedChange = { isDailyReminderEnabled = !isDailyReminderEnabled })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.account_settings),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(optionText = stringResource(id = R.string.delete_account), optionIcon = Icons.Default.Close, onClick = onDeleteAccountClicked)

        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(optionText = stringResource(id = R.string.sign_out_message), optionIcon = Icons.Default.ExitToApp, onClick = onSignOutClicked)

        Spacer(modifier = Modifier.height(6.dp))

        SettingsCardItem(optionText = stringResource(id = R.string.clear_diary), optionIcon = Icons.Default.Delete, onClick = onClearDiaryClicked)
    }
}