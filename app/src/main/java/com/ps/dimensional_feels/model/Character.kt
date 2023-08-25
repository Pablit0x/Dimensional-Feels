package com.ps.dimensional_feels.model

object CharacterNames {
    const val rick = "Rick"
    const val morty = "Morty"
    const val beth = "Beth"
    const val summer = "Summer"
    const val jerry = "Jerry"
}

sealed class RickAndMortyCharacters(val name: String) {
    object Rick : RickAndMortyCharacters(name = CharacterNames.rick)
    object Morty : RickAndMortyCharacters(name = CharacterNames.morty)
    object Beth : RickAndMortyCharacters(name = CharacterNames.beth)
    object Summer : RickAndMortyCharacters(name = CharacterNames.summer)
    object Jerry : RickAndMortyCharacters(name = CharacterNames.jerry)
}

fun String.toRickAndMortyCharacter(): RickAndMortyCharacters {
    return when (this) {
        CharacterNames.rick -> RickAndMortyCharacters.Rick
        CharacterNames.morty -> RickAndMortyCharacters.Morty
        CharacterNames.beth -> RickAndMortyCharacters.Beth
        CharacterNames.summer -> RickAndMortyCharacters.Summer
        CharacterNames.jerry -> RickAndMortyCharacters.Jerry
        else -> RickAndMortyCharacters.Rick
    }
}
