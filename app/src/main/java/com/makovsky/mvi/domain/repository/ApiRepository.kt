package com.makovsky.mvi.domain.repository

import com.makovsky.mvi.data.remote.ApiServices
import com.makovsky.mvi.data.remote.model.toAllPokemonsList
import com.makovsky.mvi.data.remote.model.toPokemonDetails
import com.makovsky.mvi.data.remote.model.toPokemonSpecies
import com.makovsky.mvi.domain.entities.Pokemon
import com.makovsky.mvi.domain.entities.PokemonDetails
import com.makovsky.mvi.domain.entities.PokemonSpecies
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiServices: ApiServices,
) {
    suspend fun getAllPokemons(limit: Int, offset: Int): List<Pokemon>? =
        apiServices.getAllPokemons(limit, offset).toAllPokemonsList()

    suspend fun getPokemonDetails(name: String): PokemonDetails =
        apiServices.getPokemonDetails(name).toPokemonDetails()

    suspend fun getPokemonSpecies(id: Int): PokemonSpecies =
        apiServices.getPokemonSpecies(id).toPokemonSpecies()
}