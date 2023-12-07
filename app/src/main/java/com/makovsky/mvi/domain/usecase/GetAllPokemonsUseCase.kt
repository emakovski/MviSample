package com.makovsky.mvi.domain.usecase

import com.makovsky.mvi.base.BaseUseCase
import com.makovsky.mvi.domain.entities.Pokemon
import com.makovsky.mvi.domain.repository.ApiRepository
import com.makovsky.mvi.utils.Either
import javax.inject.Inject

class GetAllPokemonsUseCase @Inject constructor(private val apiRepository: ApiRepository) :
    BaseUseCase<List<Pokemon>, GetAllPokemonsUseCase.Param>() {

    override suspend fun run(param: Param): Either<Any, List<Pokemon>> {
        return try {
            val pokemons = apiRepository.getAllPokemons(
                limit = param.limit,
                offset = param.offset
            ) ?: listOf()
            Either.Right(pokemons)
        } catch (exception: Exception){
            Either.Left(onWrapException(exception))
        }
    }
    data class Param (val limit: Int, val offset: Int)
}