package com.makovsky.mvi.data.remote.model

import com.google.gson.annotations.SerializedName
import com.makovsky.mvi.domain.entities.Pokemon

data class RemotePokemon (
    @SerializedName("name")
    val name: String = "",
    @SerializedName("url")
    val url: String = ""
)

fun RemotePokemon.toPokemon(): Pokemon {
    val number = url.substring(0, url.length - 1).substringAfterLast("/")
    val formattedNumber = when (number.length) {
        1 -> String.format("#00%s", number)
        2 -> String.format("#0%s", number)
        else -> String.format("#%s", number)
    }
    return Pokemon(name.uppercase(), url, formattedNumber)
}