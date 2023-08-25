package com.ps.dimensional_feels.model

import androidx.compose.ui.graphics.Color
import com.ps.dimensional_feels.presentation.theme.AmorousColor
import com.ps.dimensional_feels.presentation.theme.AngryColor
import com.ps.dimensional_feels.presentation.theme.ConfusedColor
import com.ps.dimensional_feels.presentation.theme.DepressedColor
import com.ps.dimensional_feels.presentation.theme.DrunkColor
import com.ps.dimensional_feels.presentation.theme.HappyColor

sealed class Mood(
    val contentColor: Color, val containerColor: Color, val icon: Int
) {
    class Happy(character: Character, val name: String = "Happy") : Mood(
        icon = when (character) {
            Character.RICK -> MoodIcon.HappyRick.icon
            Character.MORTY -> MoodIcon.Default.icon
            Character.BETH -> MoodIcon.Default.icon
            Character.JERRY -> MoodIcon.Default.icon
            Character.SUMMER -> MoodIcon.Default.icon
        },
        contentColor = Color.Black, containerColor = HappyColor,
    )

    data class Angry(val character: Character) : Mood(
        icon = when (character) {
            Character.RICK -> MoodIcon.AngryRick.icon
            Character.MORTY -> MoodIcon.Default.icon
            Character.BETH -> MoodIcon.Default.icon
            Character.JERRY -> MoodIcon.Default.icon
            Character.SUMMER -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = AngryColor
    )

    data class Confused(val character: Character) : Mood(
        icon = when (character) {
            Character.RICK -> MoodIcon.ConfusedRick.icon
            Character.MORTY -> MoodIcon.Default.icon
            Character.BETH -> MoodIcon.Default.icon
            Character.JERRY -> MoodIcon.Default.icon
            Character.SUMMER -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = ConfusedColor
    )

    data class Depressed(val character: Character) : Mood(
        icon = when (character) {
            Character.RICK -> MoodIcon.DepressedRick.icon
            Character.MORTY -> MoodIcon.Default.icon
            Character.BETH -> MoodIcon.Default.icon
            Character.JERRY -> MoodIcon.Default.icon
            Character.SUMMER -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = DepressedColor
    )

    data class Amorous(val character: Character) : Mood(
        icon = when (character) {
            Character.RICK -> MoodIcon.AmorousRick.icon
            Character.MORTY -> MoodIcon.Default.icon
            Character.BETH -> MoodIcon.Default.icon
            Character.JERRY -> MoodIcon.Default.icon
            Character.SUMMER -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = AmorousColor
    )

    data class Drunk(val character: Character) : Mood(
        icon = when (character) {
            Character.RICK -> MoodIcon.DrunkRick.icon
            Character.MORTY -> MoodIcon.Default.icon
            Character.BETH -> MoodIcon.Default.icon
            Character.JERRY -> MoodIcon.Default.icon
            Character.SUMMER -> MoodIcon.Default.icon
        }, contentColor = Color.Black, containerColor = DrunkColor
    )
}