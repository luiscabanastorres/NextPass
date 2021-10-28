package com.nextpass.nextiati.nextpass.extensions

import com.nextpass.nextiati.nextpass.errorparser.ErrorParser
import com.nextpass.nextiati.nextpass.state.EmptyState
import com.nextpass.nextiati.nextpass.state.ViewModelListResult
import com.nextpass.nextiati.nextpass.state.ViewModelResult
import com.nextpass.nextiati.domain.state.Result


fun <R> Result<R>.viewModel(
    errorParser: ErrorParser,
    transform: (R) -> R = { data: R -> data }
): ViewModelResult<R> =
    when (this) {
        is Result.Success -> {
            val value = transform(data)
            ViewModelResult.Success(value)
        }
        is Result.Cached -> {
            val value = transform(data)
            ViewModelResult.Cached(value)
        }
        is Result.Unauthorized -> {
            ViewModelResult.Unauthorized("Error", "sin autorización")
        }
        is Result.Error -> {
            val error = errorParser.parse(ex)
            ViewModelResult.Error(error.first, error.second)
        }
        is Result.Loading -> ViewModelResult.Loading
    }.checkAllMatched

fun <R> Result<List<R>>.viewModelList(
    errorParser: ErrorParser,
    transform: (List<R>) -> List<R> = { list: List<R> -> list },
    emptyState: () -> EmptyState
): ViewModelListResult<R> =
    when (this) {
        is Result.Success -> if (data.isEmpty()) {
            val state = emptyState()
            ViewModelListResult.Empty(state)
        } else {
            val list = transform(data)
            ViewModelListResult.Success(list)
        }
        is Result.Cached -> if (data.isEmpty()) {
            val state = emptyState()
            ViewModelListResult.EmptyCached(state)
        } else {
            val list = transform(data)
            ViewModelListResult.Cached(list)
        }

        is Result.Error -> {
            val error = errorParser.parse(ex)
            ViewModelListResult.Error(error.first, error.second)
        }
        is Result.Loading -> {
            ViewModelListResult.Loading
        }
        is Result.Unauthorized -> {

            ViewModelListResult.Unauthorized("Error", "Sin autorización")
        }
    }.checkAllMatched
