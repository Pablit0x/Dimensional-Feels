package com.ps.dimensional_feels.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.presentation.components.DeleteDiaryDropDownMenu
import com.ps.dimensional_feels.presentation.components.TimePickerDialog
import com.ps.dimensional_feels.util.Constants.DATE_TIME_PATTERN
import com.ps.dimensional_feels.util.toInstant
import com.ps.dimensional_feels.util.toLocalDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
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
    onDeleteConfirmed: () -> Unit,
    onNavigateToDraw: () -> Unit
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }
    var isDateTimeUpdated by remember { mutableStateOf(false) }

    var currentDate by remember { mutableStateOf(LocalDate.now()) }
    var currentTime by remember { mutableStateOf(LocalTime.now()) }

    val formattedDateTime = remember(currentDate, currentTime) {
        val localDateTime = LocalDateTime.of(currentDate, currentTime)
        DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(localDateTime).uppercase()
    }

    val diaryDateInstant = remember(selectedDiary) {
        selectedDiary?.date?.toInstant() ?: LocalDateTime.now().toInstant(ZoneOffset.UTC)
    }

    val selectedDiaryDateTime = remember(diaryDateInstant) {
        SimpleDateFormat(
            DATE_TIME_PATTERN, Locale.getDefault()
        ).format(Date.from(diaryDateInstant)).uppercase()
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
                modifier = Modifier.fillMaxWidth(), text = if (isDateTimeUpdated) formattedDateTime
                else selectedDiaryDateTime, style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                ), textAlign = TextAlign.Center
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
                showDatePickerDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(id = R.string.select_date_icon)
                )
            }
        }
        DeleteDiaryDropDownMenu(
            selectedDiary = selectedDiary,
            onDeleteConfirmed = onDeleteConfirmed,
            onNavigateToDraw = onNavigateToDraw
        )
    })


    if (showDatePickerDialog) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = diaryDateInstant.toEpochMilli())

        DatePickerDialog(onDismissRequest = { showDatePickerDialog = false }, confirmButton = {
            Button(onClick = {
                datePickerState.selectedDateMillis?.let { dateMillis ->
                    currentDate = dateMillis.toLocalDate()
                }
                showDatePickerDialog = false
                showTimePickerDialog = true
            }) {
                Text(text = stringResource(id = R.string.next))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = stringResource(id = R.string.next)
                )
            }
        }, dismissButton = {
            OutlinedButton(onClick = { showDatePickerDialog = false }) {
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

    if (showTimePickerDialog) {

        val timePickerState = rememberTimePickerState(
            initialHour = diaryDateInstant.atOffset(ZoneOffset.UTC).hour,
            initialMinute = diaryDateInstant.atOffset(ZoneOffset.UTC).minute
        )


        TimePickerDialog(
            title = stringResource(id = R.string.select_time),
            onCancel = { showTimePickerDialog = false }, onConfirm = {
            currentTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
            isDateTimeUpdated = true
            onDateTimeUpdated(
                ZonedDateTime.of(
                    currentDate, currentTime, ZoneId.systemDefault()
                )
            )
            showTimePickerDialog = false
        }) {
            TimePicker(state = timePickerState)
        }
    }
}