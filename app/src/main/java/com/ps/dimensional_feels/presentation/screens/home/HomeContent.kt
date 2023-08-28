package com.ps.dimensional_feels.presentation.screens.home


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.presentation.components.DateHeader
import com.ps.dimensional_feels.presentation.components.DiaryHolder
import com.ps.dimensional_feels.presentation.components.EmptyPage
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    diariesNotes: Map<LocalDate, List<Diary>>,
    onClick: (String) -> Unit
) {
    if (diariesNotes.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(all = 24.dp)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            diariesNotes.forEach { (localDate, diaries) ->
                stickyHeader(localDate) {
                    DateHeader(localDate = localDate)
                }


                items(diaries, key = { it._id.toString() }) { diary ->
                    DiaryHolder(diary = diary, onClick = { onClick(diary._id.toHexString()) })
                }
            }
        }
    } else {
        EmptyPage()
    }
}