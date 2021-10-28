package com.nextpass.nextiati.domain.state

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Cached<out T>(val data: T) : Result<T>()

    data class Unauthorized(val ex: Exception) : Result<Nothing>()

    data class Error(val ex: Exception) : Result<Nothing>()


    object Loading : Result<Nothing>()

}
