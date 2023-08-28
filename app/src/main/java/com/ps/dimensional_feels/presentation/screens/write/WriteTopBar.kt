package com.ps.dimensional_feels.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.presentation.components.DeleteDiaryDropDownMenu
import com.ps.dimensional_feels.util.Constants.DATE_PATTERN
import com.ps.dimensional_feels.util.Constants.DATE_TIME_PATTERN
import com.ps.dimensional_feels.util.Constants.TIME_PATTERN
import com.ps.dimensional_feels.util.toInstant
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: Diary?,
    moodName: () -> String,
    onBackPressed: () -> Unit,
    onDateTimeUpdated: (ZonedDateTime?) -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    val dateDialog = rememberUseCaseState()
    val timeDialog = rememberUseCaseState()
    var isDateTimeUpdated by remember { mutableStateOf(false) }
    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    val formattedDate = remember(currentDate) {
        DateTimeFormatter.ofPattern(DATE_PATTERN).format(currentDate).uppercase()
    }
    val formattedTime = remember(currentTime) {
        DateTimeFormatter.ofPattern(TIME_PATTERN).format(currentTime).uppercase()
    }

    val selectedDiaryDateTime = remember(selectedDiary) {
        if (selectedDiary != null) {
            SimpleDateFormat(
                DATE_TIME_PATTERN, Locale.getDefault()
            ).format(Date.from(selectedDiary.date.toInstant())).uppercase()
        } else {
            "Unknown"
        }
    }
    CenterAlignedTopAppBar(navigationIcon = {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.navigate_back)
            )
        }
    }, title = {
        Column {
            Text(
                text = moodName(),
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (selectedDiary != null && isDateTimeUpdated) "$formattedDate, $formattedTime"
                else if (selectedDiary != null) selectedDiaryDateTime
                else "$formattedDate, $formattedTime",
                style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                ),
                textAlign = TextAlign.Center
            )
        }
    }, actions = {
        if (isDateTimeUpdated) {
            IconButton(onClick = {
                currentDate = LocalDate.now()
                currentTime = LocalTime.now()
                isDateTimeUpdated = false
                onDateTimeUpdated(null)
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
        if (selectedDiary != null) {
            DeleteDiaryDropDownMenu(
                selectedDiary = selectedDiary,
                onDeleteConfirmed = { onDeleteConfirmed() })
        }
    })

    CalendarDialog(
        state = dateDialog, selection = CalendarSelection.Date { localDate ->
            currentDate = localDate
            timeDialog.show()
        }, config = CalendarConfig(monthSelection = true, yearSelection = false)
    )

    ClockDialog(state = timeDialog, selection = ClockSelection.HoursMinutes { hours, minutes ->
        currentTime = LocalTime.of(hours, minutes)
        isDateTimeUpdated = true
        onDateTimeUpdated(ZonedDateTime.of(currentDate, currentTime, ZoneId.systemDefault()))
    })


}