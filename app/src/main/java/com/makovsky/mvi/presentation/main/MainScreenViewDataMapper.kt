package com.makovsky.mvi.presentation.main

import com.makovsky.mvi.domain.entities.Pokemon
import javax.inject.Inject

class MainScreenViewDataMapper @Inject constructor() {

    fun buildScreen(pokemons: List<Pokemon>?): List<MainScreenItem> {
        val viewData = mutableListOf<MainScreenItem>()
        viewData.addAll(pokemons?.map { pokemon ->
            MainScreenItem.MainScreenPokemonItem(
                pokemon.name,
                pokemon.url,
            )
        } ?: emptyList())
        return viewData
    }
}