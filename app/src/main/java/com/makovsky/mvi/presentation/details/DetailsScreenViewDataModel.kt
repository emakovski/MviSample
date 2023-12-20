package com.makovsky.mvi.presentation.details

import com.makovsky.mvi.domain.entities.PokemonDetails
import com.makovsky.mvi.domain.entities.PokemonSpecies

sealed class DetailsScreenItem {
    data class DetailsScreenPokemonInfoItem(
        val pokemonDetails: PokemonDetails,
        val pokemonSpecies: PokemonSpecies,
    ) : DetailsScreenItem()
}