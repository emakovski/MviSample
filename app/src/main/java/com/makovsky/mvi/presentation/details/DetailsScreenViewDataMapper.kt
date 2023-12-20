package com.makovsky.mvi.presentation.details

import com.makovsky.mvi.domain.entities.PokemonInfo
import javax.inject.Inject

class DetailsScreenViewDataMapper @Inject constructor() {

    fun buildScreen(pokemonInfo: PokemonInfo?): DetailsScreenItem? {
        return pokemonInfo?.let {
            DetailsScreenItem.DetailsScreenPokemonInfoItem(
                it.pokemonDetails,
                it.pokemonSpecies
            )
        }
    }
}