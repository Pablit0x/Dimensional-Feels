package com.ps.dimensional_feels.model

import androidx.compose.ui.graphics.Color
import com.ps.dimensional_feels.presentation.theme.AmorousColor
import com.ps.dimensional_feels.presentation.theme.AngryColor
import com.ps.dimensional_feels.presentation.theme.ConfusedColor
import com.ps.dimensional_feels.presentation.theme.DepressedColor
import com.ps.dimensional_feels.presentation.theme.DrunkColor
import com.ps.dimensional_feels.presentation.theme.HappyColor

open class Mood(
    val contentColor: Color, val containerColor: Color, val icon: Int, val name: String,
) {
    class Happy(character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.HappyRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = HappyColor, name = "Happy"
    ) {
        companion object {
            const val name: String = "Happy"
        }
    }

    data class Angry(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.AngryRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = AngryColor, name = "Angry"
    ) {
        companion object {
            const val name: String = "Angry"
        }
    }

    data class Confused(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.ConfusedRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = ConfusedColor, name = "Confused"
    ) {
        companion object {
            const val name: String = "Confused"
        }
    }

    data class Depressed(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) :
        Mood(
            icon = when (character) {
                RickAndMortyCharacters.Rick -> MoodIcon.DepressedRick.icon
                RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
                RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
                RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
                RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
            }, contentColor = Color.Black, containerColor = DepressedColor, name = "Depressed"
        ) {
        companion object {
            const val name: String = "Depressed"
        }
    }

    data class Amorous(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.AmorousRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = AmorousColor, name = "Amorous"
    ) {
        companion object {
            const val name: String = "Amorous"
        }
    }

    data class Drunk(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.DrunkRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = DrunkColor, name = "Drunk"
    ) {
        companion object {
            const val name: String = "Drunk"
        }
    }
}

fun getMoodByName(
    name: String, character: RickAndMortyCharacters = RickAndMortyCharacters.Rick
): Mood {
    return when (name) {
        "Happy" -> Mood.Happy(character = character)
        "Angry" -> Mood.Angry(character = character)
        "Confused" -> Mood.Angry(character = character)
        "Depressed" -> Mood.Depressed(character = character)
        "Amorous" -> Mood.Amorous(character = character)
        "Drunk" -> Mood.Drunk(character = character)
        else -> Mood.Happy(character = character)
    }
}