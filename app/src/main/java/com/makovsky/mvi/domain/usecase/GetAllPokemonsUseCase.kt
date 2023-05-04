package com.makovsky.mvi.domain.usecase

import com.makovsky.mvi.base.BaseUseCase
import com.makovsky.mvi.domain.entities.Pokemon
import com.makovsky.mvi.domain.repository.ApiRepository
import com.makovsky.mvi.utils.Either
import javax.inject.Inject

class GetAllPokemonsUseCase @Inject constructor(private val apiRepository: ApiRepository) :
    BaseUseCase<List<Pokemon>, BaseUseCase.None>() {

    override suspend fun run(param: None): Either<Any, List<Pokemon>> {
        return try {
            val pokemons = apiRepository.getAllPokemons() ?: listOf()
            Either.Right(pokemons)
        } catch (exception: Exception){
            Either.Left(onWrapException(exception))
        }
    }
}