package com.makovsky.mvi.data.remote

import com.makovsky.mvi.data.remote.model.AllPokemonsResponse
import retrofit2.http.GET

interface ApiServices {

    @GET("pokemon/?limit=1000")
    suspend fun getAllPokemons(): AllPokemonsResponse
}