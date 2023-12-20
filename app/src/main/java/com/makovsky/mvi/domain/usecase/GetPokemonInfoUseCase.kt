package com.makovsky.mvi.domain.usecase

import com.makovsky.mvi.base.BaseUseCase
import com.makovsky.mvi.domain.entities.PokemonInfo
import com.makovsky.mvi.domain.repository.ApiRepository
import com.makovsky.mvi.utils.Either
import javax.inject.Inject

class GetPokemonInfoUseCase @Inject constructor(private val apiRepository: ApiRepository) :
    BaseUseCase<PokemonInfo, GetPokemonInfoUseCase.Param>() {

    override suspend fun run(param: Param): Either<Any, PokemonInfo> {
        return try {
            val pokemonDetails = apiRepository.getPokemonDetails(param.name)
            val pokemonSpecies = apiRepository.getPokemonSpecies(pokemonDetails.id)
            Either.Right(PokemonInfo(pokemonDetails, pokemonSpecies))
        } catch (exception: Exception){
            Either.Left(onWrapException(exception))
        }
    }
    data class Param (val name: String)
}