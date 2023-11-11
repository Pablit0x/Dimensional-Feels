package com.ps.dimensional_feels.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary

@Composable
fun DeleteDiaryDropDownMenu(
    selectedDiary: Diary?, onDeleteConfirmed: () -> Unit, onNavigateToDraw: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

        if (selectedDiary != null) {
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

        DropdownMenuItem(leadingIcon = {
            Icon(
                imageVector = Icons.Default.Brush, contentDescription = stringResource(
                    id = R.string.navigate_to_canvas
                ), tint = MaterialTheme.colorScheme.primary
            )
        }, text = {
            Text(text = stringResource(id = R.string.canvas))
        }, onClick = {
            onNavigateToDraw()
            expanded = false
        })
    }
    CustomAlertDialog(title = stringResource(id = R.string.delete),
        message = stringResource(id = R.string.delete_diary_message),
        isOpen = openDialog,
        onCloseDialog = { openDialog = false },
        onConfirmClicked = { onDeleteConfirmed() })
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.overflow_menu_icon),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}