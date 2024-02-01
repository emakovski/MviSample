package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.PokemonDetails

data class RemotePokemonDetails (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("base_experience")
    val baseExperience: Int,
    @SerializedName("order")
    val order: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("types")
    val types: List<RemoteType>,
    @SerializedName("stats")
    val stats: List<RemoteStat>,
    @SerializedName("sprites")
    val sprites: RemoteSprites,
)

fun RemotePokemonDetails.toPokemonDetails(): PokemonDetails = PokemonDetails(
    id,
    name,
    baseExperience,
    order,
    height,
    weight,
    types.map { it.toType() },
    stats.map { it.toStat() },
    sprites.toSprites()
)