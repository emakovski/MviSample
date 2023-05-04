package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Pokemon

class RemotePokemon {
    @SerializedName("name")
    val name: String = ""
    @SerializedName("url")
    val url: String = ""
}

fun RemotePokemon.toPokemon(): Pokemon =
    Pokemon(name, url)