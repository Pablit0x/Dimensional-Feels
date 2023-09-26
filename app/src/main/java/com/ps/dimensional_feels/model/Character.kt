package com.ps.dimensional_feels.model

object CharacterNames {
    const val RICK = "Rick"
    const val MORTY = "Morty"
    const val BETH = "Beth"
    const val SUMMER = "Summer"
    const val JERRY = "Jerry"
}

sealed class RickAndMortyCharacters(val name: String) {
    data object Rick : RickAndMortyCharacters(name = CharacterNames.RICK)
    data object Morty : RickAndMortyCharacters(name = CharacterNames.MORTY)
    data object Beth : RickAndMortyCharacters(name = CharacterNames.BETH)
    data object Summer : RickAndMortyCharacters(name = CharacterNames.SUMMER)
    data object Jerry : RickAndMortyCharacters(name = CharacterNames.JERRY)
}

fun String.toRickAndMortyCharacter(): RickAndMortyCharacters {
    return when (this) {
        CharacterNames.RICK -> RickAndMortyCharacters.Rick
        CharacterNames.MORTY -> RickAndMortyCharacters.Morty
        CharacterNames.BETH -> RickAndMortyCharacters.Beth
        CharacterNames.SUMMER -> RickAndMortyCharacters.Summer
        CharacterNames.JERRY -> RickAndMortyCharacters.Jerry
        else -> RickAndMortyCharacters.Rick
    }
}
