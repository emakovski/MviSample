package com.makovsky.mvi.domain.entities

import com.makovsky.mvi.data.remote.model.Stat

data class PokemonDetails(
    val id: Int,
    val name: String,
    val baseExperience: Int,
    val order: Int,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val stats: List<Stat>,
    val sprites: Sprites,
)