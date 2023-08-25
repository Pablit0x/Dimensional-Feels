package com.ps.dimensional_feels.model

import com.ps.dimensional_feels.R

sealed class MoodIcon(val icon: Int) {
    object Default : MoodIcon(icon = R.drawable.google_logo)
    object HappyRick : MoodIcon(icon = R.drawable.happy_rick)
    object AngryRick : MoodIcon(icon = R.drawable.angry_rick)
    object DrunkRick : MoodIcon(icon = R.drawable.drunk_rick)
    object DepressedRick : MoodIcon(icon = R.drawable.depressed_rick)
    object ConfusedRick : MoodIcon(icon = R.drawable.confused_rick)
    object AmorousRick : MoodIcon(icon = R.drawable.amorous_rick)
}


enum class Character {
    RICK, MORTY, SUMMER, BETH, JERRY
}