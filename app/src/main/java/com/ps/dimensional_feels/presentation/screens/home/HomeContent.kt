package com.ps.dimensional_feels.presentation.screens.home


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.presentation.components.DiaryHolder
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    diariesNotes: Map<LocalDate, List<Diary>>, onClick: (String) -> Unit
) {
    if (diariesNotes.isNotEmpty()) {
        LazyColumn(modifier = modifier) {
            diariesNotes.forEach { (localDate, diaries) ->
                stickyHeader(localDate) {
                    DateHeader(localDate = localDate)
                }

                items(diaries, key = { it._id }) { diary ->
                    DiaryHolder(diary = diary, onClick = onClick)
                }
            }
        }
    } else {
        EmptyDiaryPage()
    }
}

@Composable
fun DateHeader(localDate: LocalDate) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = String.format("%02d", localDate.dayOfMonth), style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )

            Text(
                text = localDate.dayOfWeek.toString().take(3), style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = localDate.month.toString().lowercase().replaceFirstChar { it.titlecase() },
                style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )

            Text(
                text = "${localDate.year}", color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.40f
                ), style = TextStyle.Default.copy(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }
    }
}

@Composable
fun EmptyDiaryPage(
    title: String = stringResource(id = R.string.empty_diary_title),
    description: String = stringResource(id = R.string.empty_diary_description)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title, style = TextStyle.Default.copy(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )

        Text(
            text = description, style = TextStyle.Default.copy(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        )

    }
}