package com.makovsky.mvi.presentation.main

import com.makovsky.mvi.base.UiEvent
import com.makovsky.mvi.base.UiState
import javax.annotation.concurrent.Immutable

@Immutable
sealed class MainScreenUiEvent : UiEvent {
    object LoadingStarted : MainScreenUiEvent()
    data class ShowData(val items: List<MainScreenItem>) : MainScreenUiEvent()
    data class ShowLastData(val items: List<MainScreenItem>) : MainScreenUiEvent()
}

@Immutable
data class MainScreenState(
    val isLoading: Boolean,
    val data: List<MainScreenItem>,
    val allPokemonsLoaded: Boolean
) : UiState {

    companion object {
        fun initial() = MainScreenState(
            isLoading = true,
            data = emptyList(),
            allPokemonsLoaded = false
        )
    }

    override fun toString(): String {
        return "isLoading: $isLoading, data.size: ${data.size}, allPokemonsLoaded: $allPokemonsLoaded"
    }
}