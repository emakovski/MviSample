package com.makovsky.mvi.presentation.main

import android.util.Log
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
    private val getAllPokemonsUseCase: GetAllPokemonsUseCase,
    private val viewMapper: MainScreenViewDataMapper,
) : BaseViewModel<MainScreenState, MainScreenUiEvent>() {

    private val reducer = MainReducer(MainScreenState.initial())

    override val state: StateFlow<MainScreenState>
        get() = reducer.state

    val timeMachine: TimeCapsule<MainScreenState>
        get() = reducer.timeCapsule

    private var offset: Int = 0

    init { loadPokemons() }

    fun loadPokemons(){
        sendEvent(MainScreenUiEvent.LoadingStarted)
        getAllPokemonsUseCase.invoke(viewModelScope, GetAllPokemonsUseCase.Param(
            limit = LIMIT,
            offset = offset
        )){
            it.either(::onError, ::onPokemonsLoaded)
        }
    }

    private fun onError(any: Any){
        Log.e(TAG, "loadPokemons(offset=$offset) failed")
    }

    private fun onPokemonsLoaded(newPortion: List<Pokemon>){
        if (newPortion.isEmpty()){
            sendEvent(MainScreenUiEvent.ShowLastData(viewMapper.buildScreen(newPortion)))
        } else {
            offset += newPortion.size
            if (reducer.state.value.data.isEmpty()){
                if (newPortion.size < LIMIT){
                    sendEvent(MainScreenUiEvent.ShowLastData(viewMapper.buildScreen(newPortion)))
                } else {
                    sendEvent(MainScreenUiEvent.ShowData(viewMapper.buildScreen(newPortion)))
                }
            } else {
                val pokemonsWithNewPortion = mutableListOf<MainScreenItem>()
                pokemonsWithNewPortion.addAll(reducer.state.value.data)
                pokemonsWithNewPortion.addAll(viewMapper.buildScreen(newPortion))
                if (newPortion.size < LIMIT){
                    sendEvent(MainScreenUiEvent.ShowLastData(pokemonsWithNewPortion))
                } else {
                    sendEvent(MainScreenUiEvent.ShowData(pokemonsWithNewPortion))
                }
            }
        }
    }

    private fun sendEvent(event: MainScreenUiEvent) {
        reducer.sendEvent(event)
    }

    private class MainReducer(initial: MainScreenState) : Reducer<MainScreenState, MainScreenUiEvent>(initial) {
        override fun reduce(oldState: MainScreenState, event: MainScreenUiEvent) {
            when (event) {
                is MainScreenUiEvent.LoadingStarted -> {
                    setState(oldState.copy(isLoading = true))
                }
                is MainScreenUiEvent.ShowData -> {
                    setState(oldState.copy(isLoading = false, data = event.items))
                }
                is MainScreenUiEvent.ShowLastData -> {
                    setState(oldState.copy(isLoading = false, data = event.items, allPokemonsLoaded = true))
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val LIMIT = 10
    }
}