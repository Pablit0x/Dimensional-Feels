package com.ps.dimensional_feels.presentation.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.presentation.components.TimePickerDialog
import com.ps.dimensional_feels.util.toLocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClicked: () -> Unit,
    dateIsSelected: Boolean,
    onDateSelected: (ZonedDateTime) -> Unit,
    searchActive: Boolean,
    onSearchClicked: () -> Unit,
    onDateReset: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    TopAppBar(
        scrollBehavior = scrollBehavior, navigationIcon = {
        IconButton(onClick = onMenuClicked) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.hamburger_menu_icon)
            )
        }
    }, title = {
        Text(text = stringResource(id = R.string.home_top_bar_title))
    }, actions = {
        if (dateIsSelected) {
            IconButton(onClick = {
                onDateReset()
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.reset_date_icon)
                )
            }
        } else {
            IconButton(onClick = {
                showDatePicker = true
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.select_date_icon)
                )
            }
        }

        if (showDatePicker) {
            val datePickerState =
                rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
            DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis ->
                        onDateSelected(
                            ZonedDateTime.of(
                                dateMillis.toLocalDate(), LocalTime.now(), ZoneId.systemDefault()
                            )
                        )
                    }
                    showDatePicker = false
                }) {
                    Text(text = stringResource(id = R.string.apply))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(id = R.string.apply_selected_date_filter)
                    )
                }
            }, dismissButton = {
                OutlinedButton(onClick = { showDatePicker = false }) {
                    Text(text = stringResource(id = R.string.dismiss))
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.dismiss_date_dialog)
                    )
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }

        IconButton(onClick = {
            onSearchClicked()
        }) {
            Icon(
                imageVector = if(!searchActive) Icons.Default.Search else Icons.Default.SearchOff,
                contentDescription = stringResource(id = androidx.compose.material3.R.string.search_bar_search)
            )
        }
    })
}