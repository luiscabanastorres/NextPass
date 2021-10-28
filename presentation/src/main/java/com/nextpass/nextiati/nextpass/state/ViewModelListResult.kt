package com.nextpass.nextiati.nextpass.state

import androidx.annotation.StringRes

sealed class ViewModelListResult<out R> {

    data class Success<out T>(val data: List<T>) : ViewModelListResult<T>()

    data class Cached<out T>(val data: List<T>) : ViewModelListResult<T>()

    data class Empty(val state: EmptyState) : ViewModelListResult<Nothing>()

    data class EmptyCached(val state: EmptyState) : ViewModelListResult<Nothing>()

    data class Error(
         val title: String,
        val message: String,
    ) : ViewModelListResult<Nothing>()
    data class Unauthorized(
        val title: String,
        val message: String,
    ) : ViewModelListResult<Nothing>()

    object Loading : ViewModelListResult<Nothing>()

}
