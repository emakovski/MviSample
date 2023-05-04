package com.makovsky.mvi.presentation.main

import androidx.lifecycle.viewModelScope
import com.makovsky.mvi.base.BaseUseCase
import com.makovsky.mvi.base.BaseViewModel
import com.makovsky.mvi.base.Reducer
import com.makovsky.mvi.base.TimeCapsule
import com.makovsky.mvi.domain.entities.Pokemon
import com.makovsky.mvi.domain.usecase.GetAllPokemonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getAllPokemonsUseCase: GetAllPokemonsUseCase,
    private val viewMapper: MainScreenViewDataMapper,
) : BaseViewModel<MainScreenState, MainScreenUiEvent>() {

    private val reducer = MainReducer(MainScreenState.initial())

    override val state: StateFlow<MainScreenState>
        get() = reducer.state

    val timeMachine: TimeCapsule<MainScreenState>
        get() = reducer.timeCapsule

    init {
        getAllPokemonsUseCase.invoke(viewModelScope, BaseUseCase.None()){
            it.either({}, ::onPokemonsLoaded)
        }
    }

    private fun onPokemonsLoaded(pokemons: List<Pokemon>){
        sendEvent(MainScreenUiEvent.ShowData(viewMapper.buildScreen(pokemons)))
    }

    private fun sendEvent(event: MainScreenUiEvent) {
        reducer.sendEvent(event)
    }

    private class MainReducer(initial: MainScreenState) : Reducer<MainScreenState, MainScreenUiEvent>(initial) {
        override fun reduce(oldState: MainScreenState, event: MainScreenUiEvent) {
            when (event) {
                is MainScreenUiEvent.ShowData -> {
                    setState(oldState.copy(isLoading = false, data = event.items))
                }
            }
        }
    }
}