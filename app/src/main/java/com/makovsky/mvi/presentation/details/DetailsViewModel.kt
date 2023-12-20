package com.makovsky.mvi.presentation.details

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.makovsky.mvi.base.BaseViewModel
import com.makovsky.mvi.base.Reducer
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.domain.entities.PokemonInfo
import com.makovsky.mvi.domain.usecase.GetPokemonInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getPokemonInfoUseCase: GetPokemonInfoUseCase,
    private val viewMapper: DetailsScreenViewDataMapper,
): BaseViewModel<DetailsScreenState, DetailsScreenUiEvent>() {

    private val reducer = DetailsReducer(DetailsScreenState.initial())

    override val state: StateFlow<DetailsScreenState>
        get() = reducer.state

    val timeMachine: TimeCapsule<DetailsScreenState>
        get() = reducer.timeCapsule

    private var pokemon: String = ""

    fun launchPokemon(name: String) {
        sendEvent(DetailsScreenUiEvent.LoadingStarted)
        pokemon = name
        getPokemonInfoUseCase.invoke(viewModelScope, GetPokemonInfoUseCase.Param(name.lowercase())){
            it.either(::onError, ::onPokemonLoaded)
        }
    }

    private fun onError(any: Any){
        Log.e(TAG, "launchPokemon(name=$pokemon) failed")
        sendEvent(DetailsScreenUiEvent.ShowData(null))
    }

    private fun onPokemonLoaded(pokemonInfo: PokemonInfo){
        sendEvent(DetailsScreenUiEvent.ShowData(viewMapper.buildScreen(pokemonInfo)))
    }

    private fun sendEvent(event: DetailsScreenUiEvent) {
        reducer.sendEvent(event)
    }

    private class DetailsReducer(initial: DetailsScreenState) : Reducer<DetailsScreenState, DetailsScreenUiEvent>(initial) {
        override fun reduce(oldState: DetailsScreenState, event: DetailsScreenUiEvent) {
            when (event) {
                is DetailsScreenUiEvent.LoadingStarted -> {
                    setState(oldState.copy(isLoading = true))
                }
                is DetailsScreenUiEvent.ShowData -> {
                    setState(oldState.copy(isLoading = false, data = event.item))
                }
            }
        }
    }

    companion object {
        private const val TAG = "DetailsViewModel"
    }
}