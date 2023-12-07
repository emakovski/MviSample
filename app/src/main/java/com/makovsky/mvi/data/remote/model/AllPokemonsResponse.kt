package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Pokemon

class AllPokemonsResponse {
    @SerializedName("results")
    val results: List<RemotePokemon>? = null
}

fun AllPokemonsResponse.toAllPokemonsList(): List<Pokemon>? =
    results?.map { it.toPokemon() }