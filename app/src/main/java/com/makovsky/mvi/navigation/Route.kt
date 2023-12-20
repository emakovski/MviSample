package com.makovsky.mvi.navigation

object Route {
    const val route = "main_route"
    const val defaultDestination = "main_destination"
    const val detail = "main_detail/{pokemon}"

    fun detail(pokemon: String) = "main_detail/$pokemon"
}