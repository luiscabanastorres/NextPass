package com.nextpass.nextiati.nextpass.state

sealed class ViewModelResult<out R> {

    data class Success<out T>(val data: T) : ViewModelResult<T>()

    data class Cached<out T>(val data: T) : ViewModelResult<T>()

    data class Error(
         val title: String,
        val message: String,
    ) : ViewModelResult<Nothing>()
    data class Unauthorized(
        val title: String,
        val message: String,
    ) : ViewModelResult<Nothing>()

    object Loading : ViewModelResult<Nothing>()

}
