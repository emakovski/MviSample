package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Description
import com.makovsky.mvi.domain.entities.PokemonSpecies

data class RemotePokemonSpecies (
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<RemoteDescription>,
)

fun RemotePokemonSpecies.toPokemonSpecies(): PokemonSpecies {
    val setOfDesc = mutableSetOf<String>()
    val listOfDesc = flavorTextEntries.map { it.toDescription() }
    val newListOfDesc = mutableListOf<Description>()
    listOfDesc.forEach { desc ->
        desc.flavorText?.let { setOfDesc.add(it) }
    }
    setOfDesc.forEach {
        newListOfDesc.add(Description(it))
    }
    return PokemonSpecies(newListOfDesc)
}