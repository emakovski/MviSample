package com.makovsky.mvi.presentation.main

sealed class MainScreenItem {
    data class MainScreenPokemonItem(
        val name: String,
        val url: String,
        val number: String,
    ) : MainScreenItem()
}
