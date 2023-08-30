package com.ps.util.model

import com.ps.util.R

sealed class MoodIcon(val icon: Int) {
    object Default : MoodIcon(icon = R.drawable.google_logo)
    object HappyRick : MoodIcon(icon = R.drawable.happy_rick)
    object AngryRick : MoodIcon(icon = R.drawable.angry_rick)
    object DrunkRick : MoodIcon(icon = R.drawable.drunk_rick)
    object DepressedRick : MoodIcon(icon = R.drawable.depressed_rick)
    object ConfusedRick : MoodIcon(icon = R.drawable.confused_rick)
    object AmorousRick : MoodIcon(icon = R.drawable.amorous_rick)
}
