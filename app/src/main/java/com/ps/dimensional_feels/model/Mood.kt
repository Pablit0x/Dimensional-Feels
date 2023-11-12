package com.ps.dimensional_feels.model

import androidx.compose.ui.graphics.Color
import com.ps.dimensional_feels.presentation.theme.LovingColor
import com.ps.dimensional_feels.presentation.theme.AngryColor
import com.ps.dimensional_feels.presentation.theme.ConfusedColor
import com.ps.dimensional_feels.presentation.theme.DepressedColor
import com.ps.dimensional_feels.presentation.theme.DrunkColor
import com.ps.dimensional_feels.presentation.theme.HappyColor

sealed class Mood(
    val onContainerColor: Color, val containerColor: Color, val icon: Int, val name: String,
) {

    companion object {
        const val MOOD_COUNT: Int = 6
    }

    class Happy(character: RickAndMortyCharacters) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.HappyRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.HappyMorty.icon
            RickAndMortyCharacters.Beth -> MoodIcon.HappyBeth.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.HappyJerry.icon
            RickAndMortyCharacters.Summer -> MoodIcon.HappySummer.icon
        }, onContainerColor = Color.White, containerColor = HappyColor, name = name
    ) {
        companion object {
            const val name: String = "Happy"
            const val position = 0
        }
    }

    data class Angry(val character: RickAndMortyCharacters) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.AngryRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.AngryMorty.icon
            RickAndMortyCharacters.Beth -> MoodIcon.AngryBeth.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.AngryJerry.icon
            RickAndMortyCharacters.Summer -> MoodIcon.AngrySummer.icon
        }, onContainerColor = Color.White, containerColor = AngryColor, name = name
    ) {
        companion object {
            const val name: String = "Angry"
            const val position = 1
        }
    }

    data class Confused(val character: RickAndMortyCharacters) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.ConfusedRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.ConfusedMorty.icon
            RickAndMortyCharacters.Beth -> MoodIcon.ConfusedBeth.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.ConfusedJerry.icon
            RickAndMortyCharacters.Summer -> MoodIcon.ConfusedSummer.icon
        }, onContainerColor = Color.Black, containerColor = ConfusedColor, name = name
    ) {
        companion object {
            const val name: String = "Confused"
            const val position = 2
        }
    }

    data class Depressed(val character: RickAndMortyCharacters) :
        Mood(
            icon = when (character) {
                RickAndMortyCharacters.Rick -> MoodIcon.DepressedRick.icon
                RickAndMortyCharacters.Morty -> MoodIcon.DepressedMorty.icon
                RickAndMortyCharacters.Beth -> MoodIcon.DepressedBeth.icon
                RickAndMortyCharacters.Jerry -> MoodIcon.DepressedJerry.icon
                RickAndMortyCharacters.Summer -> MoodIcon.DepressedSummer.icon
            }, onContainerColor = Color.Black, containerColor = DepressedColor, name = name
        ) {
        companion object {
            const val name: String = "Depressed"
            const val position = 3
        }
    }

    data class Loving(val character: RickAndMortyCharacters) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.AmorousRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.AmorousMorty.icon
            RickAndMortyCharacters.Beth -> MoodIcon.AmorousBeth.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.AmorousJerry.icon
            RickAndMortyCharacters.Summer -> MoodIcon.AmorousSummer.icon
        }, onContainerColor = Color.White, containerColor = LovingColor, name = name
    ) {
        companion object {
            const val name: String = "Loving"
            const val position = 4
        }
    }

    data class Tipsy(val character: RickAndMortyCharacters) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.DrunkRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.DrunkMorty.icon
            RickAndMortyCharacters.Beth -> MoodIcon.DrunkBeth.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.DrunkJerry.icon
            RickAndMortyCharacters.Summer -> MoodIcon.DrunkSummer.icon
        }, onContainerColor = Color.White, containerColor = DrunkColor, name = name
    ) {
        companion object {
            const val name: String = "Tipsy"
            const val position = 5
        }
    }
}

fun getMoodByName(
    name: String, character: RickAndMortyCharacters
): Mood {
    return when (name) {
        Mood.Happy.name -> Mood.Happy(character = character)
        Mood.Angry.name -> Mood.Angry(character = character)
        Mood.Confused.name -> Mood.Confused(character = character)
        Mood.Depressed.name -> Mood.Depressed(character = character)
        Mood.Loving.name -> Mood.Loving(character = character)
        Mood.Tipsy.name -> Mood.Tipsy(character = character)
        else -> Mood.Happy(character = character)
    }
}

fun getMoodByPosition(character: RickAndMortyCharacters, position: Int): Mood {
    return when (position) {
        Mood.Happy.position -> Mood.Happy(character = character)
        Mood.Angry.position -> Mood.Angry(character = character)
        Mood.Confused.position -> Mood.Confused(character = character)
        Mood.Loving.position -> Mood.Loving(character = character)
        Mood.Tipsy.position -> Mood.Tipsy(character = character)
        Mood.Depressed.position -> Mood.Depressed(character = character)
        else -> Mood.Happy(character = character)
    }
}

fun getPositionByMood(mood: Mood): Int {
    return when (mood) {
        is Mood.Happy -> Mood.Happy.position
        is Mood.Angry -> Mood.Angry.position
        is Mood.Confused -> Mood.Confused.position
        is Mood.Loving -> Mood.Loving.position
        is Mood.Tipsy -> Mood.Tipsy.position
        is Mood.Depressed -> Mood.Depressed.position
        else -> Mood.Happy.position
    }
}