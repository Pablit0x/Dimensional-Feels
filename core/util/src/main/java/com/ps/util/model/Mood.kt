package com.ps.util.model

import androidx.compose.ui.graphics.Color
import com.ps.util.Constants.AmorousColor
import com.ps.util.Constants.AngryColor
import com.ps.util.Constants.ConfusedColor
import com.ps.util.Constants.DepressedColor
import com.ps.util.Constants.DrunkColor
import com.ps.util.Constants.HappyColor

open class Mood(
    val onContainerColor: Color, val containerColor: Color, val icon: Int, val name: String,
) {
    class Happy(character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.HappyRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, onContainerColor = Color.Black, containerColor = HappyColor, name = "Happy"
    ) {
        companion object {
            const val name: String = "Happy"
            const val position = 0
        }
    }

    data class Angry(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.AngryRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, onContainerColor = Color.White, containerColor = AngryColor, name = "Angry"
    ) {
        companion object {
            const val name: String = "Angry"
            const val position = 1
        }
    }

    data class Confused(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.ConfusedRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, onContainerColor = Color.White, containerColor = ConfusedColor, name = "Confused"
    ) {
        companion object {
            const val name: String = "Confused"
            const val position = 2
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
            }, onContainerColor = Color.Black, containerColor = DepressedColor, name = "Depressed"
        ) {
        companion object {
            const val name: String = "Depressed"
            const val position = 3
        }
    }

    data class Amorous(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.AmorousRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, onContainerColor = Color.White, containerColor = AmorousColor, name = "Amorous"
    ) {
        companion object {
            const val name: String = "Amorous"
            const val position = 4
        }
    }

    data class Drunk(val character: RickAndMortyCharacters = RickAndMortyCharacters.Rick) : Mood(
        icon = when (character) {
            RickAndMortyCharacters.Rick -> MoodIcon.DrunkRick.icon
            RickAndMortyCharacters.Morty -> MoodIcon.Default.icon
            RickAndMortyCharacters.Beth -> MoodIcon.Default.icon
            RickAndMortyCharacters.Jerry -> MoodIcon.Default.icon
            RickAndMortyCharacters.Summer -> MoodIcon.Default.icon
        }, onContainerColor = Color.Black, containerColor = DrunkColor, name = "Drunk"
    ) {
        companion object {
            const val name: String = "Drunk"
            const val position = 5
        }
    }
}

fun getMoodByName(
    name: String, character: RickAndMortyCharacters = RickAndMortyCharacters.Rick
): Mood {
    return when (name) {
        Mood.Happy.name -> Mood.Happy(character = character)
        Mood.Angry.name -> Mood.Angry(character = character)
        Mood.Confused.name -> Mood.Confused(character = character)
        Mood.Depressed.name -> Mood.Depressed(character = character)
        Mood.Amorous.name -> Mood.Amorous(character = character)
        Mood.Drunk.name -> Mood.Drunk(character = character)
        else -> Mood.Happy(character = character)
    }
}

fun getMoodByPosition(character: RickAndMortyCharacters, position: Int): Mood {
    return when (position) {
        Mood.Happy.position -> Mood.Happy(character = character)
        Mood.Angry.position -> Mood.Angry(character = character)
        Mood.Confused.position -> Mood.Confused(character = character)
        Mood.Amorous.position -> Mood.Amorous(character = character)
        Mood.Drunk.position -> Mood.Drunk(character = character)
        Mood.Depressed.position -> Mood.Depressed(character = character)
        else -> Mood.Happy(character = character)
    }
}

fun getPositionByMood(mood: Mood): Int {
    return when (mood) {
        is Mood.Happy -> Mood.Happy.position
        is Mood.Angry -> Mood.Angry.position
        is Mood.Confused -> Mood.Confused.position
        is Mood.Amorous -> Mood.Amorous.position
        is Mood.Drunk -> Mood.Drunk.position
        is Mood.Depressed -> Mood.Depressed.position
        else -> Mood.Happy.position
    }
}