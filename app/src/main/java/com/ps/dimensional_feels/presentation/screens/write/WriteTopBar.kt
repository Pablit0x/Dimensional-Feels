package com.ps.dimensional_feels.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.presentation.components.CustomAlertDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: Diary?, onBackPressed: () -> Unit, onDeleteConfirmed: () -> Unit
) {
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
                text = "Happy", modifier = Modifier.fillMaxWidth(), style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ), textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "26 SEP 2023, 10:00 AM",
                style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                ),
                textAlign = TextAlign.Center
            )
        }
    }, actions = {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(id = R.string.select_date_icon)
            )
        }
        if (selectedDiary != null) {
            DeleteDiaryAction(selectedDiary = selectedDiary, onDeleteConfirmed = onDeleteConfirmed)
        }
    })
}


@Composable
fun DeleteDiaryAction(
    selectedDiary: Diary?, onDeleteConfirmed: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(leadingIcon = {
            Icon(
                imageVector = Icons.Default.Delete, contentDescription = stringResource(
                    id = R.string.delete_diary
                ), tint = MaterialTheme.colorScheme.error
            )
        }, text = {
            Text(text = stringResource(id = R.string.delete))
        }, onClick = {
            openDialog = true
            expanded = false
        })
    }
    CustomAlertDialog(
        title = stringResource(id = R.string.delete),
        message = "${stringResource(id = R.string.delete_diary_message)}: ${selectedDiary?.title}?",
        isOpen = openDialog,
        onCloseDialog = { openDialog = false },
        onConfirmClicked = onDeleteConfirmed
    )
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.overflow_menu_icon),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}