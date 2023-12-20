package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.PokemonDetails

data class RemotePokemonDetails (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("sprites")
    val sprites: RemoteSprites,
)

fun RemotePokemonDetails.toPokemonDetails(): PokemonDetails = PokemonDetails(
    id,
    name,
    sprites.toSprites()
)