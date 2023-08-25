package com.ps.dimensional_feels.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ps.dimensional_feels.R
import com.ps.dimensional_feels.model.Diary
import com.ps.dimensional_feels.model.Mood
import com.ps.dimensional_feels.model.RickAndMortyCharacters
import com.ps.dimensional_feels.model.getMoodByName
import com.ps.dimensional_feels.model.toRickAndMortyCharacter
import com.ps.dimensional_feels.presentation.theme.Elevation
import com.ps.dimensional_feels.util.toInstant
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DiaryHolder(
    diary: Diary, onClick: (String) -> Unit
) {
    var localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    Row(modifier = Modifier.clickable(indication = null, interactionSource = remember {
        MutableInteractionSource()
    }) { onClick(diary.diaryId.toString()) }) {
        Spacer(modifier = Modifier.width(14.dp))
        Surface(modifier = Modifier
            .width(2.dp)
            .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1,
            content = {})
        Spacer(modifier = Modifier.width(20.dp))
        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                }, tonalElevation = Elevation.Level1
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DiaryHeader(
                    character = diary.character.toRickAndMortyCharacter(),
                    moodName = diary.mood,
                    time = diary.date.toInstant()
                )
                Text(
                    modifier = Modifier.padding(14.dp),
                    text = diary.description,
                    style = TextStyle.Default.copy(
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    ),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun DiaryHeader(character: RickAndMortyCharacters, moodName: String, time: Instant) {
    val mood by remember { mutableStateOf(getMoodByName(name = moodName, character = character)) }
    val formatter = remember {
        DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = mood.icon),
                contentDescription = stringResource(id = R.string.mood_icon),
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }
        Text(
            text = formatter.format(time),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }
}


@Preview
@Composable
fun holderPreview() {
    DiaryHolder(diary = Diary().apply {
        title = "Random Ass Title"
        description = "Some random Text"
        mood = Mood.Happy.name
        character = RickAndMortyCharacters.Rick.name
    }, onClick = {})
}