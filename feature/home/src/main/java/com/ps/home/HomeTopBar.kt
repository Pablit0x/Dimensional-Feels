package com.ps.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.ps.util.R
import java.time.LocalDate
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
    onDateReset: () -> Unit
) {
    val dateDialog = rememberUseCaseState()
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }

    TopAppBar(scrollBehavior = scrollBehavior, navigationIcon = {
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
                dateDialog.show()
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.select_date_icon)
                )
            }
        }

        CalendarDialog(state = dateDialog, selection = CalendarSelection.Date { localDate ->
            pickedDate = localDate
            onDateSelected(ZonedDateTime.of(pickedDate, LocalTime.now(), ZoneId.systemDefault()))
        }, config = CalendarConfig(monthSelection = true, yearSelection = true))

    })
}