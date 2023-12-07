package com.makovsky.mvi.base

import com.makovsky.mvi.data.network.Failure
import com.makovsky.mvi.utils.Either
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseUseCase<out Type, in Param> where Type : Any {

    abstract suspend fun run(param: Param): Either<Any, Type>

    open val dispatcher: CoroutineDispatcher = Dispatchers.Default

    open operator fun invoke(
        scope: CoroutineScope,
        param: Param,
        result: (Either<Any, Type>) -> Unit = {}
    ): Job {
        val backgroundJob = scope.async(dispatcher) { run(param) }
        return scope.launch(Dispatchers.Main) { result.invoke(backgroundJob.await()) }
    }

    protected fun onWrapException(exception: Exception): Any {
        return try {
            when (exception) {
                is UnknownHostException -> Failure.NetworkFailure(exception)
                is ConnectException -> Failure.NetworkFailure(exception)
                else -> Failure.UnknownFailure(exception)
            }
        } catch (exception: Exception) {
            Failure.UnknownFailure(exception)
        }
    }

    class None
}