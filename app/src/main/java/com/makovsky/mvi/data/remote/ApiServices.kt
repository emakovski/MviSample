package com.makovsky.mvi.data.remote

import com.makovsky.mvi.data.remote.model.AllPokemonsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("pokemon/")
    suspend fun getAllPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): AllPokemonsResponse
}