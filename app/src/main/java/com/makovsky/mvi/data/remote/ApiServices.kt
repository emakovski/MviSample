package com.makovsky.mvi.data.remote

import com.makovsky.mvi.data.remote.model.AllPokemonsResponse
import com.makovsky.mvi.data.remote.model.RemotePokemonDetails
import com.makovsky.mvi.data.remote.model.RemotePokemonSpecies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("pokemon/")
    suspend fun getAllPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): AllPokemonsResponse

    @GET("pokemon/{name}/")
    suspend fun getPokemonDetails(@Path("name") name: String): RemotePokemonDetails

    @GET("pokemon-species/{id}/")
    suspend fun getPokemonSpecies(@Path("id") id: Int): RemotePokemonSpecies
}