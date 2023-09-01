package com.ps.dimensional_feels.model

import com.ps.dimensional_feels.R

sealed class MoodIcon(val icon: Int) {
    // Rick
    object HappyRick : MoodIcon(icon = R.drawable.happy_rick)
    object AngryRick : MoodIcon(icon = R.drawable.angry_rick)
    object DrunkRick : MoodIcon(icon = R.drawable.drunk_rick)
    object DepressedRick : MoodIcon(icon = R.drawable.depressed_rick)
    object ConfusedRick : MoodIcon(icon = R.drawable.confused_rick)
    object AmorousRick : MoodIcon(icon = R.drawable.amorous_rick)

    // Morty
    object HappyMorty : MoodIcon(icon = R.drawable.happy_morty)
    object AngryMorty : MoodIcon(icon = R.drawable.angry_morty)
    object DrunkMorty : MoodIcon(icon = R.drawable.drunk_morty)
    object DepressedMorty : MoodIcon(icon = R.drawable.depressed_morty)
    object ConfusedMorty : MoodIcon(icon = R.drawable.confused_morty)
    object AmorousMorty : MoodIcon(icon = R.drawable.amorous_morty)

    // Beth
    object HappyBeth : MoodIcon(icon = R.drawable.happy_beth)
    object AngryBeth : MoodIcon(icon = R.drawable.angry_beth)
    object DrunkBeth : MoodIcon(icon = R.drawable.drunk_beth)
    object DepressedBeth : MoodIcon(icon = R.drawable.depressed_beth)
    object ConfusedBeth : MoodIcon(icon = R.drawable.confused_beth)
    object AmorousBeth : MoodIcon(icon = R.drawable.amorous_beth)

    // Summer
    object HappySummer : MoodIcon(icon = R.drawable.happy_summer)
    object AngrySummer : MoodIcon(icon = R.drawable.angry_summer)
    object DrunkSummer : MoodIcon(icon = R.drawable.drunk_summer)
    object DepressedSummer : MoodIcon(icon = R.drawable.depressed_summer)
    object ConfusedSummer : MoodIcon(icon = R.drawable.confused_summer)
    object AmorousSummer : MoodIcon(icon = R.drawable.amorous_summer)

    // Jerry
    object HappyJerry : MoodIcon(icon = R.drawable.happy_jerry)
    object AngryJerry : MoodIcon(icon = R.drawable.angry_jerry)
    object DrunkJerry : MoodIcon(icon = R.drawable.drunk_jerry)
    object DepressedJerry : MoodIcon(icon = R.drawable.depressed_jerry)
    object ConfusedJerry : MoodIcon(icon = R.drawable.confused_jerry)
    object AmorousJerry : MoodIcon(icon = R.drawable.amorous_jerry)

}
