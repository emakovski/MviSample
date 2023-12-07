package com.makovsky.mvi.domain.entities

data class Pokemon(
    val name: String,
    val url: String,
    val number: String,
) {
    companion object {
        fun mock() = Pokemon(
            name = "Pikachu",
            url = "",
            number = "001"
        )
    }
}
