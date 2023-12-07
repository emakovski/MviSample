package com.makovsky.mvi.domain.repository

import com.makovsky.mvi.data.remote.ApiServices
import com.makovsky.mvi.data.remote.model.toAllPokemonsList
import com.makovsky.mvi.domain.entities.Pokemon
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiServices: ApiServices,
) {
    suspend fun getAllPokemons(limit: Int, offset: Int): List<Pokemon>? =
        apiServices.getAllPokemons(limit, offset).toAllPokemonsList()
}