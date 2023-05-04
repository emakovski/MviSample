package com.makovsky.mvi.data.network

sealed class Failure(val exception: Exception) {
    open class UnknownFailure(exception: Exception) : Failure(exception)
    open class NetworkFailure(exception: Exception) : Failure(exception)
}